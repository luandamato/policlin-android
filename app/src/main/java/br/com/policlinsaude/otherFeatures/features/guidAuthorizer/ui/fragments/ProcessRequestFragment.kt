package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.components.bottomsheet.BottomSheetCommon
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentProcessRequestBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.CityModel
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.ProcessRequestAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import com.policlinsaude.newfeature.features.tickets.ui.fragments.TicketsFragment
import com.policlinsaude.newfeature.utils.DialogHelper
import com.policlinsaude.newfeature.utils.toDDMMYYYY
import com.shockwave.pdfium.PdfiumCore
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.*
import java.util.*


class ProcessRequestFragment : Fragment() {

    private lateinit var binding: FragmentProcessRequestBinding
    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()
    private val adapter by lazy { ProcessRequestAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProcessRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onGetCities()
        viewModel.processInialized = true
        setupViews()
        setupListeners()
        setupObservables()
        verifyButtonEnabled()
    }

    override fun onResume() {
        super.onResume()
        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        with(binding) {

            recyclerViewIncomeTax.adapter = adapter
            viewModel.processRequestModel.sdtAutCabecalho.let {
                textviewName.apply {
                    text = "${viewModel.beneficiario?.matricula} ${viewModel.beneficiario?.ordem} ${viewModel.beneficiario?.Nome_Beneficiario}"
                    isVisible = it.interlocutor.isNotEmpty()
                }
//                requestDataInclude.textviewSchedule.text = it.referenteCOVID
                serviceLocationInclude.apply {
                    textviewSchedule.text = it.agendado
                    textviewServiceData.apply {
                        text = it.dataAtendimento.toDDMMYYYY()
                        isEnabled = it.agendado.lowercase() != "não"
                    }
                    textviewCitySelected.text = it.cidadeDes
                    linearOtherCity.isVisible = it.cidadeDes.lowercase().contains("outra")
                    editCidade.setText(it.cidadeAtendimento)
                    editServiceLocation.setText(it.prestador)
                }

            }
        }

        (activity as GuideAuthorizerActivity).showButtonEdit(false)
        (activity as GuideAuthorizerActivity).showButtonCancel(false)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })
    }

    private fun setupListeners() {
        with(binding) {
            linearBeneficiaryData.setOnClickListener {
                if (viewModel.isListBeneficiaryEnable()){
                    viewModel.dependets.let {
                        if (!it.value?.getData()?.listaBeneficiario.isNullOrEmpty() && it.value?.getData()?.listaBeneficiario?.size!! > 1){
                            findNavController().navigate(R.id.beneficiary_list_fragment)
                        }
                        else{
                            findNavController().navigate(R.id.beneficiary_data_fragment)
                        }
                    }
                }
                else{
                    findNavController().navigate(R.id.beneficiary_data_fragment)
                }
            }

            buttonPicture.setOnClickListener {
                bottomSheetPicture()
            }

            adapter.setOnClickListener {
                viewModel.attachmentResponse.remove(it)
                adapter.update(viewModel.attachmentResponse)

                verifyButtonEnabled()
            }

//            requestDataInclude.textviewSchedule.setOnClickListener {
//                bottomSheetRequestData()
//            }

            serviceLocationInclude.apply {
                textviewSchedule.setOnClickListener {
                    bottomSheetSchedule()
                }

                textviewServiceData.setOnClickListener {
                    showDatePicker()
                }

                textviewCitySelected.setOnClickListener {
                    bottomSheetCities(viewModel.cities.value?.getData()?.sdtAutCidades)
                }

                editServiceLocation.doAfterTextChanged {
                    it?.let {
                        viewModel.setLocationService(it.toString())
                        verifyButtonEnabled()
                    }
                }

                editCidade.doAfterTextChanged {
                    it?.let {
                        viewModel.setCityDesc(it.toString())
                        verifyButtonEnabled()
                    }
                }
            }

            consulting.setOnClickListener {
                findNavController().navigate(ProcessRequestFragmentDirections.processRequestToRequestDeadlinesFragment())
            }

            buttonProcessRequest.setOnClickListener {
                viewModel.onPostRequestGuideAuthorizer()
                viewModel.clearUserAll()
            }

        }
    }

    private fun setupObservables() {
        with(viewModel) {
            authorizer.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        viewModel.onPostRequestGuideAuthorizerPhotos(it.getData()?.sdtAutCabecalho?.numeroWEB.orEmpty())
                    }
                    ViewModelResponseStatus.FAILED -> {
                        hideLoading()
                        messageError(it.getData()?.msgExterna.orEmpty())
                    }
                }
            }

            photosResponse.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        if(it.getData()?.codAcao == 1) {
                            viewModel.fotos = arrayListOf()
                            messageSuccess()
                        } else
                            messageError(it.getData()?.msgExterna.orEmpty())
                    }
                    else -> {
                        hideLoading()
                        if(it?.getData() != null) {
                            messageError(it.getData()?.msgExterna.orEmpty())
                        } else {
                            messageErrorAttachment()
                        }
                    }
                }
            }
        }
    }


    private fun messageSuccess() {
        DialogHelper.showDialog(
            requireContext(),
            "Sucesso!",
            "Autorizador de Guia foi enviado com sucesso",
            messagePositiveButton = "Voltar",
            listenerPositiveButton = {
                onBack()
            },
        )
    }

    private fun messageError(messagem: String) {
        DialogHelper.showErrorDialog(
            requireContext(),
            messagem,
        )
    }

    private fun messageErrorAttachment() {
        DialogHelper.showDialog(
            requireContext(),
            "Error!",
            "Ocorreu erro ao enviar os Anexos.",
            messagePositiveButton = "Voltar",
            listenerPositiveButton = {
                onBack()
            },
        )
    }

    private fun onBack() {
        viewModel.processInialized = false
        viewModel.isFromActivity = true
        viewModel.onClearRequest()
        findNavController().popBackStack()
    }

//    private fun bottomSheetRequestData() {
//        BottomSheetCommon(
//            title = "Selecione Referente ao teste de COVID-19",
//            list = arrayListOf("Sim", "Não"),
//            buttonTitle = "Confirmar",
//            buttonCancel = "Cancelar",
//            onClickListenerNext = { item ->
//                item?.let {
//                    with(binding) {
//                        requestDataInclude.textviewSchedule.text = item
//                        viewModel.setRequestData(item)
//                    }
//                }
//                verifyButtonEnabled()
//            },
//            onClickListenerClean = {
//            }
//        ).show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
//    }

    private fun bottomSheetPicture() {
        val bottomSheetCommon = BottomSheetCommon()
        bottomSheetCommon.apply {
            list = mutableListOf(TAKE_PICTURE, GALLERY, CANCEL)
            onItemSelected = { items ->
                items?.let {
                    when(it) {
                        TAKE_PICTURE -> checkPermissionsCamera()
                        GALLERY -> checkPermissionsGallery()
                    }
                    dismissAllowingStateLoss()
                }
            }
            isVisibleClearFilter = false
            isVisibleButtonApply = false
        }.show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
    }

    private fun bottomSheetSchedule() {
        BottomSheetCommon(
            title = "Selecione se já agendou a consulta ou não",
            list = arrayListOf("Sim", "Não"),
            buttonTitle = "Confirmar",
            buttonCancel = "Cancelar",
            onClickListenerNext = { item ->
                item?.let {
                    with(binding) {
                        serviceLocationInclude.textviewSchedule.text = it
                        serviceLocationInclude.textviewServiceData.apply {
                            isEnabled = item.lowercase() != "não"
                            if(item.lowercase() == "não") {
                                text = ""
                                viewModel.setDataService("")
                            }

                        }

                        viewModel.setSchedule(item)
                    }
                }
                verifyButtonEnabled()
            },
            onClickListenerClean = {
            }
        ).show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
    }

    private fun bottomSheetCities(cities: MutableList<CityModel>?) {
        BottomSheetCommon(
            title = "Selecione a cidade",
            description = "ao selecionar Outra irá abrir um campo para ser preenchido com o nome da cidade",
            list = cities?.map { it.descricao }?.toMutableList() ?: arrayListOf(),
            buttonTitle = "Confirmar",
            buttonCancel = "Cancelar",
            onClickListenerNext = { item ->
                item?.let {
                    binding.serviceLocationInclude.textviewCitySelected.text = it
                    binding.serviceLocationInclude.linearOtherCity.isVisible = it.lowercase().contains("outra")
                    viewModel.setCity(item)
                }
                verifyButtonEnabled()
            },
            onClickListenerClean = {
            }
        ).show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
    }

    @SuppressLint("SetTextI18n")
    fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val listener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            binding.serviceLocationInclude.textviewServiceData.text = "$dayOfMonth/${monthOfYear + 1}/$year"
            viewModel.setDataService("$year-${monthOfYear + 1}-$dayOfMonth")
            verifyButtonEnabled()
        }

        val datePickerDialog = DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun openCamera() {
        /*try {
          val values = ContentValues()
           values.put(MediaStore.Images.Media.TITLE, "New Picture")
           values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
           val uri: Uri? = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
           val cameraIntent = Intent(ACTION_IMAGE_CAPTURE)
           cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
           startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
        } catch (e: Exception) { }*/

        EasyImage.openCamera(this@ProcessRequestFragment, REQUEST_CODE_CAMERA)
    }


    private fun openGallery() {
        try {
            val ACCEPT_MIME_TYPES = arrayOf(
                "application/pdf",
                "image/*"
            )
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES)
            startActivityForResult(
                Intent.createChooser(intent, "Selecione a foto ou documento"),
                REQUEST_CODE_G4ALLERY
            )
        } catch (e: Exception) { }
    }

    private fun checkPermissionsGallery() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermissionAfterTiramissu()
        }else{
            requestPermissionBeforeTiramissu()
        }
    }

    private fun requestPermissionBeforeTiramissu() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> openGallery()

            shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE_GALLERY
                )
            }

            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE_GALLERY
                );
            }
        }
    }
    private fun requestPermissionAfterTiramissu() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> openGallery()

            shouldShowRequestPermissionRationale(READ_MEDIA_IMAGES) -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(READ_MEDIA_IMAGES),
                    REQUEST_PERMISSION_CODE_GALLERY
                )
            }

            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(READ_MEDIA_IMAGES),
                    REQUEST_PERMISSION_CODE_GALLERY
                );
            }
        }
    }

    private fun checkPermissionsCamera() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> openCamera()

            shouldShowRequestPermissionRationale(CAMERA) -> {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(CAMERA), REQUEST_PERMISSION_CODE_CAMERA
                )
            }
            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(CAMERA), REQUEST_PERMISSION_CODE_CAMERA
                );
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            nestedScroolView.alpha = .1F
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            nestedScroolView.alpha = 1F
            progressBar.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode) {
            RESULT_OK -> {
                when(requestCode) {
                    7459 -> {
                        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object: EasyImage.Callbacks {
                            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {

                            }

                            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                                val bitmap = BitmapFactory.decodeFile(imageFile?.absolutePath);
                                viewModel.attachmentResponse.add(
                                    viewModel.attachmentResponseJPG(bitmap, format = ".jpeg", null)
                                )
                                adapter.update(viewModel.attachmentResponse)
                            }

                            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                                if (source === EasyImage.ImageSource.CAMERA) {
                                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(context)
                                    photoFile?.delete()
                                }
                            }

                        })
                    }
                    REQUEST_CODE_G4ALLERY -> {
                        var bitmap = MediaStore.Images.Media.getBitmap(
                            context?.contentResolver,
                           data?.data
                        )

                        var format = ".jpeg"
                        var file: File? = null
                        if(bitmap == null) {
                            bitmap = data?.data?.let { generateImageFromPdf(it) }
                            format = ".pdf"
                            file = data?.data?.let { fileFromContentUri(it) }
                        }
                        if(file != null && file.absoluteFile != null && file.absoluteFile.length() > 2000000L) {
                            messageError("não é possível adicionar o arquivo, Limite de tamanho de 2MB excedido.")
                        } else {
                            viewModel.attachmentResponse.add(
                                viewModel.attachmentResponseJPG(bitmap = bitmap, format = format, file = file)
                            )
                        }


                        adapter.update(viewModel.attachmentResponse)
                    }
                }

                verifyButtonEnabled()
            }
        }
    }

    fun fileFromContentUri(contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context?.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context?.contentResolver?.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(uri: Uri): String? {
        val fileType: String? = context?.contentResolver?.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    private fun generateImageFromPdf(pdfUri: Uri): Bitmap? {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(context)
        var bmp: Bitmap? = null
        try {
            val fd: ParcelFileDescriptor? =
                requireContext().contentResolver.openFileDescriptor(pdfUri, "r")
            val pdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width: Int = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height: Int = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
            pdfiumCore.closeDocument(pdfDocument)
        } catch (e: java.lang.Exception) {

        }
        return bmp
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            REQUEST_PERMISSION_CODE_CAMERA -> openCamera()

            REQUEST_PERMISSION_CODE_GALLERY -> openGallery()
        }
    }

    companion object {
        const val TAKE_PICTURE = "Tirar Foto"
        const val GALLERY = "Abrir Galeria"
        const val CANCEL = "Cancelar"
        const val REQUEST_CODE_CAMERA = 9991
        const val REQUEST_CODE_G4ALLERY = 9990
        const val REQUEST_PERMISSION_CODE_CAMERA = 302
        const val REQUEST_PERMISSION_CODE_GALLERY = 303
        const val REQUEST_PERMISSION_CODE_READ = 60
    }

    private fun verifyButtonEnabled() {
        binding.buttonProcessRequest.isEnabled = viewModel.verifyButtonNewRequest()
    }
}