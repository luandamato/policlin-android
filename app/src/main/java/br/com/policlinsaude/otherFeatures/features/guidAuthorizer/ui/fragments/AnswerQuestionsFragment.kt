package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.policlinsaude.core.helper.PhotoPickerHelper
import br.com.policlinsaude.databinding.FragmentAnswerQuestionsBinding
import com.policlinsaude.newfeature.components.bottomsheet.BottomSheetCommon
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.ProcessRequestAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.QuestionPicturesAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import com.policlinsaude.newfeature.features.tickets.ui.fragments.TicketsFragment
import com.policlinsaude.newfeature.utils.DialogHelper
import com.policlinsaude.newfeature.utils.openBrowser
import com.policlinsaude.newfeature.utils.toDDMMYYYY
import com.policlinsaude.newfeature.utils.toHHMMSS
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class AnswerQuestionsFragment : Fragment() {

    private lateinit var binding: FragmentAnswerQuestionsBinding

    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()

    private val args by navArgs<AnswerQuestionsFragmentArgs>()

    private val adapter by lazy { QuestionPicturesAdapter() }
    private val adapter2 by lazy { ProcessRequestAdapter() }

    private lateinit var imagePickerHelper: PhotoPickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imagePickerHelper = PhotoPickerHelper(
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
                questionHour.text =
                    "Data: ${questionsAndAnswers.dataPergunta?.toDDMMYYYY()} " +
                            "Hora: ${questionsAndAnswers.dataPergunta?.toHHMMSS()}"

                answerHour.text =
                    "Data: ${questionsAndAnswers.dataResposta?.toDDMMYYYY()} " +
                            "Hora: ${questionsAndAnswers.dataResposta?.toHHMMSS()}"

                question.text = questionsAndAnswers.pergunta
                answer.text = questionsAndAnswers.resposta

                linearAnswer.isVisible = !questionsAndAnswers.resposta.isNullOrEmpty()
                buttonPicture.isVisible = questionsAndAnswers.resposta.isNullOrEmpty()
                buttonProcessRequest.isVisible = questionsAndAnswers.resposta.isNullOrEmpty()
                linearAnswerWrite.isVisible = questionsAndAnswers.resposta.isNullOrEmpty()

                recyclerViewIncomeTax.adapter =
                    if (!questionsAndAnswers.resposta.isNullOrEmpty()) {
                        adapter
                    } else {
                        adapter2
                    }

                adapter.update(questionsAndAnswers.anexos)
            }
        }
    }

    private fun setupListener() {
        with(binding) {

            buttonPicture.setOnClickListener {
                bottomSheetPicture()
            }

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
                if (it?.link?.contains(".pdf") == true) {
                    context?.openBrowser(
                        "https://docs.google.com/gview?embedded=true&url=${it.link}"
                    )
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

    private fun bottomSheetPicture() {
        BottomSheetCommon().apply {

            list = mutableListOf(
                ProcessRequestFragment.TAKE_PICTURE,
                ProcessRequestFragment.GALLERY,
                ProcessRequestFragment.CANCEL
            )

            onItemSelected = { item ->
                when (item) {
                    ProcessRequestFragment.TAKE_PICTURE -> {
                        imagePickerHelper.open(
                            PhotoPickerHelper.Mode.CAMERA
                        )
                    }

                    ProcessRequestFragment.GALLERY -> {
                        imagePickerHelper.open(
                            PhotoPickerHelper.Mode.GALLERY
                        )
                    }
                }

                dismissAllowingStateLoss()
            }

            isVisibleClearFilter = false
            isVisibleButtonApply = false

        }.show(
            childFragmentManager,
            TicketsFragment.OPEN_BOTTOM_SHEET_YEAR
        )
    }

    private fun handleImage(file: File, bitmap: Bitmap) {
        try {
            viewModel.attachmentQuestion.add(
                viewModel.attachmentResponseJPG(
                    bitmap = bitmap,
                    format = ".jpeg",
                    file = file
                )
            )

            adapter2.update(viewModel.attachmentQuestion)

        } catch (e: Exception) {
            messageError("Não foi possível processar a imagem.")
        }
    }

    private fun handlePdf(uri: Uri) {
        try {
            val file = fileFromContentUri(uri)

            if (file.length() > 2_000_000L) {
                messageError(
                    "Não é possível adicionar o arquivo, " +
                            "limite de tamanho de 2MB excedido."
                )
                return
            }

            val bitmap = generateImageFromPdf(uri)

            viewModel.attachmentQuestion.add(
                viewModel.attachmentResponseJPG(
                    bitmap = bitmap,
                    format = ".pdf",
                    file = file
                )
            )

            adapter2.update(viewModel.attachmentQuestion)

        } catch (e: Exception) {
            messageError("Não foi possível processar o PDF.")
        }
    }

    private fun verifyButtonEnabled() {
        binding.buttonProcessRequest.isEnabled =
            !binding.edittextAnswerWrite.text.isNullOrEmpty()
    }

    private fun setupObservables() {
        with(viewModel) {

            sendAnswer.observe(viewLifecycleOwner) {
                when (it.getResponseStatus()) {

                    ViewModelResponseStatus.RUNNING -> showLoading()

                    ViewModelResponseStatus.SUCCESS -> {
                        if (viewModel.attachmentQuestion.isNotEmpty()) {
                            viewModel.onPostAnswerAttachment(
                                args.numberWeb,
                                viewModel.questionsAndAnswers.perguntaID?.toInt()
                            )
                        } else {
                            hideLoading()
                            messageSuccess()
                        }
                    }

                    else -> hideLoading()
                }
            }

            sendAnswerAttachment.observe(viewLifecycleOwner) {
                when (it.getResponseStatus()) {

                    ViewModelResponseStatus.RUNNING -> showLoading()

                    ViewModelResponseStatus.SUCCESS -> {
                        if (it.getData()?.codAcao == 1) {
                            viewModel.fotos = arrayListOf()
                            viewModel.attachmentQuestion = arrayListOf()
                            messageSuccess()
                        } else {
                            messageError(
                                it.getData()?.msgExterna.orEmpty()
                            )
                        }

                        hideLoading()
                    }

                    ViewModelResponseStatus.FAILED -> {
                        hideLoading()
                        messageError(
                            it.getData()?.msgExterna.orEmpty()
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    private fun fileFromContentUri(contentUri: Uri): File {
        val extension = getFileExtension(contentUri)
        val fileName = "temp_file" +
                if (extension != null) ".${extension}" else ""

        val tempFile = File(requireContext().cacheDir, fileName)

        try {
            requireContext().contentResolver.openInputStream(contentUri).use { input ->
                FileOutputStream(tempFile).use { output ->
                    if (input != null) {
                        copy(input, output)
                    }
                }
            }
        } catch (e: Exception) {
                e.printStackTrace()
            }

        return tempFile
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val mimeType = contentResolver.getType(uri)
        val map = MimeTypeMap.getSingleton()

        return map.getExtensionFromMimeType(mimeType)
    }

    private fun copy(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int

        try {
            while (input.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun generateImageFromPdf(pdfUri: Uri): Bitmap? {
        val fileDescriptor = requireContext().contentResolver.openFileDescriptor(pdfUri, "r") ?: return null

        return try {
            PdfRenderer(fileDescriptor).use { renderer ->
                if (renderer.pageCount == 0) return null

                val page = renderer.openPage(0)
                val bitmap = Bitmap.createBitmap(
                    page.width,
                    page.height,
                    Bitmap.Config.ARGB_8888
                )

                page.render(
                    bitmap,
                    null,
                    null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                )
                page.close()

                bitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            fileDescriptor.close()
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
            "Resposta enviada com sucesso.",
            messagePositiveButton = "Voltar",
            listenerPositiveButton = {
                findNavController().navigateUp()
            }
        )
    }

    private fun messageError(message: String) {
        DialogHelper.showErrorDialog(
            requireContext(),
            message
        )
    }
}