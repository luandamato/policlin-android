package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.PhotoPickerHelper
import br.com.policlinsaude.databinding.FragmentProcessRequestBinding
import com.policlinsaude.newfeature.components.bottomsheet.BottomSheetCommon
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.CityModel
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.ProcessRequestAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import com.policlinsaude.newfeature.features.tickets.ui.fragments.TicketsFragment
import com.policlinsaude.newfeature.utils.DialogHelper
import com.policlinsaude.newfeature.utils.toDDMMYYYY
import com.shockwave.pdfium.PdfiumCore
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Calendar

class ProcessRequestFragment : Fragment() {

    private lateinit var binding: FragmentProcessRequestBinding

    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()

    private val adapter by lazy { ProcessRequestAdapter() }

    /**
     * Helper centraliza:
     * - câmera
     * - galeria
     * - permissões conforme a versão do Android
     */
    private val imagePickerHelper by lazy {
        PhotoPickerHelper(
            fragment = this,
            onImageSelected = { bitmap, file ->
                handleImage(file, bitmap)
            },
            onError = {
                messageError("Não foi possível selecionar a imagem.")
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                    text = "${viewModel.beneficiario?.matricula} " +
                            "${viewModel.beneficiario?.ordem} " +
                            "${viewModel.beneficiario?.Nome_Beneficiario}"

                    isVisible = it.interlocutor.isNotEmpty()
                }

                serviceLocationInclude.apply {
                    textviewSchedule.text = it.agendado

                    textviewServiceData.apply {
                        text = it.dataAtendimento.toDDMMYYYY()
                        isEnabled = it.agendado.lowercase() != "não"
                    }

                    textviewCitySelected.text = it.cidadeDes
                    linearOtherCity.isVisible =
                        it.cidadeDes.lowercase().contains("outra")

                    editCidade.setText(it.cidadeAtendimento)
                    editServiceLocation.setText(it.prestador)
                }
            }
        }

        (activity as GuideAuthorizerActivity).showButtonEdit(false)
        (activity as GuideAuthorizerActivity).showButtonCancel(false)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBack()
                }
            }
        )
    }

    private fun setupListeners() {
        with(binding) {

            linearBeneficiaryData.setOnClickListener {
                if (viewModel.isListBeneficiaryEnable()) {
                    viewModel.dependets.let {
                        if (
                            !it.value?.getData()?.listaBeneficiario.isNullOrEmpty() &&
                            it.value?.getData()?.listaBeneficiario?.size!! > 1
                        ) {
                            findNavController().navigate(R.id.beneficiary_list_fragment)
                        } else {
                            findNavController().navigate(R.id.beneficiary_data_fragment)
                        }
                    }
                } else {
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

            serviceLocationInclude.apply {

                textviewSchedule.setOnClickListener {
                    bottomSheetSchedule()
                }

                textviewServiceData.setOnClickListener {
                    showDatePicker()
                }

                textviewCitySelected.setOnClickListener {
                    bottomSheetCities(
                        viewModel.cities.value?.getData()?.sdtAutCidades
                    )
                }

                editServiceLocation.doAfterTextChanged {
                    viewModel.setLocationService(it?.toString().orEmpty())
                    verifyButtonEnabled()
                }

                editCidade.doAfterTextChanged {
                    viewModel.setCityDesc(it?.toString().orEmpty())
                    verifyButtonEnabled()
                }
            }

            consulting.setOnClickListener {
                findNavController().navigate(
                    ProcessRequestFragmentDirections
                        .processRequestToRequestDeadlinesFragment()
                )
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
                when (it.getResponseStatus()) {

                    ViewModelResponseStatus.RUNNING -> showLoading()

                    ViewModelResponseStatus.SUCCESS -> {
                        viewModel.onPostRequestGuideAuthorizerPhotos(
                            it.getData()
                                ?.sdtAutCabecalho
                                ?.numeroWEB
                                .orEmpty()
                        )
                    }

                    ViewModelResponseStatus.FAILED -> {
                        hideLoading()
                        messageError(it.getData()?.msgExterna.orEmpty())
                    }

                    else -> {}
                }
            }

            photosResponse.observe(viewLifecycleOwner) {
                when (it.getResponseStatus()) {

                    ViewModelResponseStatus.RUNNING -> showLoading()

                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()

                        if (it.getData()?.codAcao == 1) {
                            viewModel.fotos = arrayListOf()
                            messageSuccess()
                        } else {
                            messageError(it.getData()?.msgExterna.orEmpty())
                        }
                    }

                    else -> {
                        hideLoading()

                        if (it?.getData() != null) {
                            messageError(it.getData()?.msgExterna.orEmpty())
                        } else {
                            messageErrorAttachment()
                        }
                    }
                }
            }
        }
    }

    /**
     * O BottomSheet continua sendo usado apenas para escolher
     * a origem. A abertura de câmera/galeria e permissões ficam
     * totalmente delegadas ao ImagePickerHelper.
     */
    private fun bottomSheetPicture() {
        imagePickerHelper.open(PhotoPickerHelper.Mode.CAMERA_AND_GALLERY)
    }

    /**
     * Resultado vindo da galeria.
     *
     * Mantém o comportamento antigo:
     * - imagem -> Bitmap
     * - PDF -> primeira página convertida em Bitmap
     * - PDF original também é mantido em File
     */
    private fun handleImage(file: File, bitmap: Bitmap) {
        viewModel.attachmentResponse.add(
            viewModel.attachmentResponseJPG(
                bitmap = bitmap,
                format = ".jpeg",
                file = file
            )
        )

        adapter.update(viewModel.attachmentResponse)
        verifyButtonEnabled()
    }

    private fun bottomSheetSchedule() {
        BottomSheetCommon(
            title = "Selecione se já agendou a consulta ou não",
            list = arrayListOf("Sim", "Não"),
            buttonTitle = "Confirmar",
            buttonCancel = "Cancelar",
            onClickListenerNext = { item ->

                item?.let {
                    with(binding.serviceLocationInclude) {

                        textviewSchedule.text = it

                        textviewServiceData.apply {
                            isEnabled = it.lowercase() != "não"

                            if (it.lowercase() == "não") {
                                text = ""
                                viewModel.setDataService("")
                            }
                        }

                        viewModel.setSchedule(it)
                    }
                }

                verifyButtonEnabled()
            },
            onClickListenerClean = {}
        ).show(
            childFragmentManager,
            TicketsFragment.OPEN_BOTTOM_SHEET_YEAR
        )
    }

    private fun bottomSheetCities(cities: MutableList<CityModel>?) {
        BottomSheetCommon(
            title = "Selecione a cidade",
            description = "ao selecionar Outra irá abrir um campo para ser preenchido com o nome da cidade",
            list = cities
                ?.map { it.descricao }
                ?.toMutableList()
                ?: arrayListOf(),
            buttonTitle = "Confirmar",
            buttonCancel = "Cancelar",
            onClickListenerNext = { item ->

                item?.let {
                    binding.serviceLocationInclude.textviewCitySelected.text = it

                    binding.serviceLocationInclude.linearOtherCity.isVisible =
                        it.lowercase().contains("outra")

                    viewModel.setCity(it)
                }

                verifyButtonEnabled()
            },
            onClickListenerClean = {}
        ).show(
            childFragmentManager,
            TicketsFragment.OPEN_BOTTOM_SHEET_YEAR
        )
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            binding.serviceLocationInclude.textviewServiceData.text =
                "$day/${month + 1}/$year"

            viewModel.setDataService(
                "$year-${month + 1}-$day"
            )

            verifyButtonEnabled()
        }

        DatePickerDialog(
            requireContext(),
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
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

    private fun fileFromContentUri(contentUri: Uri): File {
        val fileExtension = getFileExtension(contentUri)
        val fileName = "temp_file" +
                if (fileExtension != null) ".$fileExtension" else ""

        val tempFile = File(
            requireContext().cacheDir,
            fileName
        )

        tempFile.createNewFile()

        try {
            FileOutputStream(tempFile).use { outputStream ->

                requireContext()
                    .contentResolver
                    .openInputStream(contentUri)
                    ?.use { inputStream ->

                        copy(
                            inputStream,
                            outputStream
                        )
                    }

                outputStream.flush()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(uri: Uri): String? {
        val fileType = requireContext()
            .contentResolver
            .getType(uri)

        return MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(
        source: InputStream,
        target: OutputStream
    ) {
        val buffer = ByteArray(8192)

        while (true) {
            val length = source.read(buffer)

            if (length <= 0) break

            target.write(buffer, 0, length)
        }
    }

    private fun generateImageFromPdf(pdfUri: Uri): Bitmap? {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(requireContext())

        var bitmap: Bitmap? = null
        var document: com.shockwave.pdfium.PdfDocument? = null

        try {
            val fileDescriptor: ParcelFileDescriptor? =
                requireContext()
                    .contentResolver
                    .openFileDescriptor(pdfUri, "r")

            if (fileDescriptor == null) {
                return null
            }

            document = pdfiumCore.newDocument(fileDescriptor)

            pdfiumCore.openPage(document, pageNumber)

            val width =
                pdfiumCore.getPageWidthPoint(
                    document,
                    pageNumber
                )

            val height =
                pdfiumCore.getPageHeightPoint(
                    document,
                    pageNumber
                )

            bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )

            pdfiumCore.renderPageBitmap(
                document,
                bitmap,
                pageNumber,
                0,
                0,
                width,
                height
            )

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                document?.let {
                    pdfiumCore.closeDocument(it)
                }
            } catch (_: Exception) {
            }
        }

        return bitmap
    }

    private fun messageSuccess() {
        DialogHelper.showDialog(
            requireContext(),
            "Sucesso!",
            "Autorizador de Guia foi enviado com sucesso",
            messagePositiveButton = "Voltar",
            listenerPositiveButton = {
                onBack()
            }
        )
    }

    private fun messageError(message: String) {
        DialogHelper.showErrorDialog(
            requireContext(),
            message
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
            }
        )
    }

    private fun onBack() {
        viewModel.processInialized = false
        viewModel.isFromActivity = true
        viewModel.onClearRequest()
        findNavController().popBackStack()
    }

    private fun verifyButtonEnabled() {
        binding.buttonProcessRequest.isEnabled =
            viewModel.verifyButtonNewRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // O helper não deve manter referência ao Fragment após sua destruição.
//        imagePickerHelper.clear()
    }

    companion object {
        const val TAKE_PICTURE = "Tirar Foto"
        const val GALLERY = "Abrir Galeria"
        const val CANCEL = "Cancelar"

        // Mantidos somente caso outras partes do projeto ainda os utilizem.
        const val REQUEST_CODE_CAMERA = 9991
        const val REQUEST_CODE_G4ALLERY = 9990
        const val REQUEST_PERMISSION_CODE_CAMERA = 302
        const val REQUEST_PERMISSION_CODE_GALLERY = 303
        const val REQUEST_PERMISSION_CODE_READ = 60
    }
}