package com.pp.chatproject

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.pp.chatproject.model.Message
import com.pp.chatproject.model.PersonConversation
import com.pp.chatproject.service.AuthService
import com.pp.chatproject.service.MessageService
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.message_row.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class ConversationActivity : AppCompatActivity() {

    companion object {
        //val timer: Timer = Timer()
        var personConversation: PersonConversation? = null

        val messageService: MessageService = MessageService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        setSupportActionBar(conversation_activity_toolbar)
        loadList()

        loadConversation()

        conversation_activity_message_send.setOnClickListener({view:View ->
            newMessage()
        })
    }

    private fun loadConversation(){
        Log.d("STEP","1")
        if(ConversationActivity.personConversation == null){return;}

        Log.d("STEP","2")
        val personConversation : PersonConversation = ConversationActivity.personConversation!!
        messageService.getConversationMessageList(personConversation.conversationId, {
            returnedList:List<Message> ->
                updateList(returnedList)
        })
    }

    private  fun updateList(list: List<Message> ){
        val adapter: MessageListAdapter = conversation_activity_message_list.adapter as MessageListAdapter
        adapter.list.clear()
        adapter.list.addAll(list)
        adapter.notifyDataSetChanged()
        Log.d("COUNT", Integer.toString(adapter.list.size))
    }
    private fun loadList(){
        conversation_activity_message_list.adapter = MessageListAdapter( this)
        val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        conversation_activity_message_list.layoutManager = layoutManager
    }
/*
    private fun initTimer(){
        timerStarted = true
        timer.scheduleAtFixedRate(500,1000){
            loadConversation()
        }
    }
*/
    private fun newMessage(){

            val message: String = conversation_activity_message_edit_text.text.toString()
            conversation_activity_message_edit_text.setText("")
            if(ConversationActivity.personConversation == null){return;}
            val personConversation : PersonConversation = ConversationActivity.personConversation!!
            messageService.sendMessage(
                    message,
                    personConversation.conversationId,
                    AuthService.getInstance().user
            ) { message ->
                if( null != message) loadConversation()
            }
}

    inner class MessageListAdapter(
            private val context: Context) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

        val list: ArrayList<Message> = ArrayList<Message>()

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val message= list[position]
            holder?.bindView(message = message)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.message_row, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindView(message: Message) {
                val messageRowLayout = itemView.massage_row_layout
                val messageRowMessage = itemView.massage_row_message
                val messageRowInfo = itemView.massage_row_info
                Log.d("Message", message.message)
                messageRowMessage.text = message.message

                val stringBuilder: StringBuilder = StringBuilder(message.info)
                stringBuilder.append(" - ")
                val dateTime:LocalDateTime = Message.getDateTimeAsLocalDateTime(message)
                if(dateTime.toLocalDate().isEqual(LocalDate.now())){
                    stringBuilder.append(dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME))
                }else{
                    stringBuilder.append(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE))
                }
                stringBuilder.append(message.dateTime)
                messageRowInfo.text = stringBuilder.toString()

                val user = AuthService.getInstance().user
                if (message.senderId == user.id) {
                    messageRowLayout.setBackgroundColor(getColor(R.color.recieved))
                } else {
                    messageRowLayout.setBackgroundColor(getColor(R.color.send))
                }

            }
        }
    }

}
