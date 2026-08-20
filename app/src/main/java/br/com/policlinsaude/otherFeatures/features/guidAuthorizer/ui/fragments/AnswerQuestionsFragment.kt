package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.policlinsaude.newfeature.components.bottomsheet.BottomSheetCommon
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentAnswerQuestionsBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.ProcessRequestAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.QuestionPicturesAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments.ProcessRequestFragment.Companion.REQUEST_CODE_CAMERA
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments.ProcessRequestFragment.Companion.REQUEST_CODE_G4ALLERY
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import com.policlinsaude.newfeature.features.tickets.ui.fragments.TicketsFragment
import com.policlinsaude.newfeature.utils.DialogHelper
import com.policlinsaude.newfeature.utils.openBrowser
import com.policlinsaude.newfeature.utils.toDDMMYYYY
import com.policlinsaude.newfeature.utils.toHHMMSS
import com.shockwave.pdfium.PdfiumCore
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.*


class AnswerQuestionsFragment : Fragment() {

    lateinit var binding: FragmentAnswerQuestionsBinding

    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()

    private val args by navArgs<AnswerQuestionsFragmentArgs>()

    private val adapter by lazy { QuestionPicturesAdapter() }
    private val adapter2 by lazy { ProcessRequestAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAnswerQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
        setupObservables()
    }

    private fun setupView() {
        with(binding) {
            with(viewModel) {
                questionHour.text = "Data: ${questionsAndAnswers.dataPergunta?.toDDMMYYYY()} Hora: ${questionsAndAnswers.dataPergunta?.toHHMMSS()}"
                answerHour.text = "Data: ${questionsAndAnswers.dataResposta?.toDDMMYYYY()} Hora: ${questionsAndAnswers.dataResposta?.toHHMMSS()}"
                question.text = questionsAndAnswers.pergunta
                answer.text = questionsAndAnswers.resposta
                linearAnswer.isVisible = !questionsAndAnswers.resposta.isNullOrEmpty()
                buttonPicture.isVisible = questionsAndAnswers.resposta.isNullOrEmpty()
                buttonProcessRequest.isVisible = questionsAndAnswers.resposta.isNullOrEmpty()
                linearAnswerWrite.isVisible = questionsAndAnswers.resposta.isNullOrEmpty()
                recyclerViewIncomeTax.adapter = if(!questionsAndAnswers.resposta.isNullOrEmpty())
                    adapter
                else
                    adapter2

                adapter.update(questionsAndAnswers.anexos)
            }
        }
    }

    private fun setupListener() {
        with(binding) {
            buttonPicture.setOnClickListener { bottomSheetPicture() }

            buttonProcessRequest.setOnClickListener {
                viewModel.onPostAnswer(
                    args.numberWeb,
                    viewModel.questionsAndAnswers.perguntaID.orEmpty(),
                    edittextAnswerWrite.text.toString()
                )
            }

            edittextAnswerWrite.doAfterTextChanged {
                verifyButtonEnabled()
            }

            adapter.setOnClickListener {
                if(it?.link?.contains(".pdf") == true) {
                    context?.openBrowser("https://docs.google.com/gview?embedded=true&url=${it.link}")
                } else {
                    context?.openBrowser(it?.link.orEmpty())
                }
            }

            adapter2.setOnClickListener {
                viewModel.attachmentQuestion.remove(it)
                adapter2.update(viewModel.attachmentQuestion)
            }
        }
    }

    private fun setupObservables() {
        with(viewModel) {
            sendAnswer.observe(viewLifecycleOwner) {
                when (it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        if(viewModel.attachmentQuestion.size > 0)
                            viewModel.onPostAnswerAttachment(args.numberWeb, viewModel.questionsAndAnswers.perguntaID?.toInt())
                        else {
                            hideLoading()
                            messageSuccess()
                        }
                    }
                    else -> hideLoading()
                }
            }

            sendAnswerAttachment.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        if(it.getData()?.codAcao == 1) {
                            viewModel.fotos = arrayListOf()
                            viewModel.attachmentQuestion = arrayListOf()
                            messageSuccess()
                        } else {
                            messageError(it.getData()?.msgExterna.orEmpty())
                        }
                        hideLoading()
                    }
                    ViewModelResponseStatus.FAILED -> {
                        hideLoading()
                        messageError(it.getData()?.msgExterna.orEmpty())
                    }
                }
            }
        }
    }

    private fun bottomSheetPicture() {
        val bottomSheetCommon = BottomSheetCommon()
        bottomSheetCommon.apply {
            list = mutableListOf(
                ProcessRequestFragment.TAKE_PICTURE,
                ProcessRequestFragment.GALLERY,
                ProcessRequestFragment.CANCEL
            )
            onItemSelected = { items ->
                items?.let {
                    when(it) {
                        ProcessRequestFragment.TAKE_PICTURE -> checkPermissionsCamera()
                        ProcessRequestFragment.GALLERY -> checkPermissionsGallery()
                    }
                    dismissAllowingStateLoss()
                }
            }
            isVisibleClearFilter = false
            isVisibleButtonApply = false
        }.show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
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
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> openGallery()

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    ProcessRequestFragment.REQUEST_PERMISSION_CODE_GALLERY
                )
            }

            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    ProcessRequestFragment.REQUEST_PERMISSION_CODE_GALLERY
                );
            }
        }
    }
    private fun requestPermissionAfterTiramissu() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> openGallery()

            shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    ProcessRequestFragment.REQUEST_PERMISSION_CODE_GALLERY
                )
            }

            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    ProcessRequestFragment.REQUEST_PERMISSION_CODE_GALLERY
                );
            }
        }
    }

    private fun checkPermissionsCamera() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> openCamera()

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.CAMERA),
                    ProcessRequestFragment.REQUEST_PERMISSION_CODE_CAMERA
                )
            }
            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.CAMERA),
                    ProcessRequestFragment.REQUEST_PERMISSION_CODE_CAMERA
                );
            }
        }
    }

    private fun openCamera() {
        try {
            EasyImage.openCamera(this@AnswerQuestionsFragment, REQUEST_CODE_CAMERA)
        } catch (e: Exception) { }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode) {
            Activity.RESULT_OK -> {
                when(requestCode) {
                    7459 -> {
                        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object: EasyImage.Callbacks {
                            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {

                            }

                            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                                val bitmap = BitmapFactory.decodeFile(imageFile?.absolutePath);
                                viewModel.attachmentQuestion.add(
                                    viewModel.attachmentResponseJPG(bitmap, format = ".jpeg", null)
                                )

                                adapter2.update(viewModel.attachmentQuestion)
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
                            viewModel.attachmentQuestion.add(
                                viewModel.attachmentResponseJPG(bitmap = bitmap, format = format, file = file)
                            )
                        }

                        adapter2.update(viewModel.attachmentQuestion)
                    }
                }
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
        } catch (e: java.lang.Exception) { }
        return bmp
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            ProcessRequestFragment.REQUEST_PERMISSION_CODE_CAMERA -> openCamera()

            ProcessRequestFragment.REQUEST_PERMISSION_CODE_GALLERY -> openGallery()
        }
    }

    private fun showLoading() {
        with(binding) {
            constraintAnswer.alpha = .1F
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            constraintAnswer.alpha = 1F
            progressBar.visibility = View.GONE
        }
    }

    private fun messageSuccess() {
        DialogHelper.showDialog(
            requireContext(),
            "Sucesso!",
            "Resposta foi enviada com sucesso",
            messagePositiveButton = "Voltar",
            listenerPositiveButton = {
                onBack()
                viewModel.clearAnswer()
            },
        )
    }

    private fun messageError(messagem: String) {
        DialogHelper.showErrorDialog(
            requireContext(),
            messagem
        )
    }

    private fun onBack() {
        viewModel.isFromActivity = true
        findNavController().popBackStack()
    }

    private fun verifyButtonEnabled() {
        binding.buttonProcessRequest.isEnabled = !binding.edittextAnswerWrite.text.isNullOrEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearAnswer()
    }
}