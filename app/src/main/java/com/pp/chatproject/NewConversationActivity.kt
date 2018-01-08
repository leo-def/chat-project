package com.pp.chatproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.pp.chatproject.model.Person
import com.pp.chatproject.model.PersonConversation
import com.pp.chatproject.service.AuthService
import com.pp.chatproject.service.PersonConversationService
import com.pp.chatproject.service.PersonService
import kotlinx.android.synthetic.main.activity_new_conversation.*
import kotlinx.android.synthetic.main.person_row.view.*

class NewConversationActivity : AppCompatActivity() {


    companion object {
        val personService: PersonService = PersonService()
        val personConversationService : PersonConversationService = PersonConversationService()
        val limit = 30
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_conversation)
        setSupportActionBar(new_conversation_activity_toolbar)
        new_conversation_activity_filter.visibility  = View.INVISIBLE
        loadList()

        personService.byFilter(null, limit,{
            personList: List<Person> ->
            updateList(personList)
            new_conversation_activity_filter.visibility = View.VISIBLE
        })

        new_conversation_activity_filter.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                personService.byFilter(s.toString(), limit,{
                    personList: List<Person> ->
                    updateList(personList)
                })
            }
        })

    }

    fun updateList(newList: List<Person>){
        val adapter : PeopleListAdapter = new_conversation_activity_person_list.adapter as PeopleListAdapter
        val list = adapter.list
        list.clear()
        list.addAll(newList)
        new_conversation_activity_person_list.adapter.notifyDataSetChanged()
    }

    fun loadList(){
        new_conversation_activity_person_list.adapter = PeopleListAdapter( this)

        val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        new_conversation_activity_person_list.layoutManager = layoutManager
    }

    private fun goToConversationActivity(person: Person){
        personConversationService.findOrCreateSimple(
                AuthService.getInstance().user,
                person,
                { personConversation : PersonConversation ->
                    val intent = Intent(this, ConversationActivity::class.java)
                    ConversationActivity.personConversation = personConversation
                    startActivity(intent)
                }

        )

    }

    inner class PeopleListAdapter(
            private val context: Context) : RecyclerView.Adapter<PeopleListAdapter.ViewHolder>() {


        val list: ArrayList<Person> = ArrayList<Person>()


        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val conversation = list[position]
            holder?.bindView(conversation)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.person_row, parent, false)

            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindView(person: Person) {
                val personname = itemView.person_row_name
				
				personname.text = person.displayName

                itemView.setOnClickListener({view:View ->
                    goToConversationActivity(person)
                })

            }

        }

    }


}
