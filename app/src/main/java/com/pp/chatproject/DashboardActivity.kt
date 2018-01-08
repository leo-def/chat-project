package com.pp.chatproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.pp.chatproject.model.Person
import com.pp.chatproject.model.PersonConversation
import com.pp.chatproject.service.AuthService
import com.pp.chatproject.service.PersonConversationService
import kotlinx.android.synthetic.main.conversation_row.view.*
import java.util.ArrayList


class DashboardActivity : AppCompatActivity() {

        companion object {
            val personConversationService : PersonConversationService = PersonConversationService()

        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_dashboard)
            setSupportActionBar(dashboard_activity_toolbar)
            loadList()

            dashboard_activity_fab.setOnClickListener { view ->
                Snackbar.make(view, "Nova Conversa", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                goToNewConversationActivity()
            }
            val user : Person = AuthService.getInstance().user
            personConversationService.simpleByPerson(
                    user,
                    {list:List<PersonConversation> ->
                        updateList(list)
                    })
        }


    private fun updateList(list: List<PersonConversation>){
        val adapter = dashboard_activity_conversation_list.adapter as ConversationListAdapter
        adapter.list.clear()
        adapter.list.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun loadList(){
        dashboard_activity_conversation_list.adapter = ConversationListAdapter( this)
        val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        dashboard_activity_conversation_list.layoutManager = layoutManager
    }

    private fun goToConversationActivity( personConversation: PersonConversation){
            val intent = Intent(this, ConversationActivity::class.java)
            ConversationActivity.personConversation = personConversation
            startActivity(intent)
        }

    private fun goToNewConversationActivity(){
            val intent = Intent(this, NewConversationActivity::class.java)
            startActivity(intent)
        }

        inner class ConversationListAdapter(
                val context: Context) : RecyclerView.Adapter<ConversationListAdapter.ViewHolder>() {

            val list: ArrayList<PersonConversation> = ArrayList<PersonConversation>()

            override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
                val personConversation: PersonConversation = list[position]
                holder?.bindView(personConversation)
            }

            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(context).inflate(R.layout.conversation_row, parent, false)
                return ViewHolder(view)
            }

            override fun getItemCount(): Int {
                return list.size
            }

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

                fun bindView(personConversation: PersonConversation) {
                    val person = itemView.conversation_row_person_name
                    val last_message = itemView.conversation_row_last_message

					person.text = personConversation.title
					last_message.text = personConversation.info
                    /*
                    person.text = dto.personName
                    last_message.text = dto.lastMessage;
					*/
                    itemView.setOnClickListener({view: View ->
                        goToConversationActivity(personConversation)
                    })
                }
            }
        }
}
