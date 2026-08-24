package com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.AdapterQuestionAndAnswerBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerQuestionsItemsModel
import com.policlinsaude.newfeature.utils.toDDMMYYYY
import com.policlinsaude.newfeature.utils.toHHMMSS


class QuestionAndAnswerAdapter (
    private var list: MutableList<GuideAuthorizerQuestionsItemsModel> = arrayListOf(),
    var setOnClickListener: (GuideAuthorizerQuestionsItemsModel?) -> Unit? = { },
) : RecyclerView.Adapter<QuestionAndAnswerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterQuestionAndAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<GuideAuthorizerQuestionsItemsModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: (GuideAuthorizerQuestionsItemsModel?) -> Unit) {
        setOnClickListener = listener
    }

    inner class ViewHolder(private val binding: AdapterQuestionAndAnswerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GuideAuthorizerQuestionsItemsModel?) {
            with(binding) {
                imageviewRight.rotation = -90f
                question.text = item?.pergunta
                questionHour.text = "${item?.dataPergunta?.toDDMMYYYY()} ás ${item?.dataPergunta?.toHHMMSS()}"
                if(!item?.resposta.isNullOrEmpty()) {
                    answer.text = item?.resposta
                    answerHour.text = "${item?.dataResposta?.toDDMMYYYY()} ás ${item?.dataResposta?.toHHMMSS()}"
                } else {
                    answer.text = "Aguardando Resposta..."
                    answerHour.text = ""
                    answer.background = null
                }

                root.setOnClickListener {
                    setOnClickListener.invoke(item)
                }

            }
        }
    }

}