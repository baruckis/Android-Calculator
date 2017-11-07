/*
 * Copyright 2017 Andrius Baruckis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baruckis.calculator

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.util.*


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You activity (or fragment) needs to implement [HistoryActionListDialogFragment.Listener].
 */
class HistoryActionListDialogFragment : BottomSheetDialogFragment() {

    // If ? is inserted after any type name, we have explicitly instructed the compiler that the value of the type can either store an object reference or can be null.
    private var mListener: Listener? = null

    private lateinit var mData: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_history_action_list_dialog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // Initializing an ArrayList in one line when fragment is already attached to Activity and string resources are available to reach.
        mData = ArrayList<String>(Arrays.asList(getString(R.string.no_history)))

        val data = arguments.getStringArrayList(ARG_HISTORY_ACTION)
        if (data.isNotEmpty()) {
            mData.clear()
            mData.addAll(data)
        }

        val recyclerView = view!!.findViewById<View>(R.id.list) as RecyclerView?
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ItemAdapter(mData)

        val buttonClearHistory: ImageButton = view.findViewById(R.id.button_clear_history)
        buttonClearHistory.setOnClickListener {
            data.clear()
            mData.clear()
            mData.add(getString(R.string.no_history))
            recyclerView.adapter.notifyDataSetChanged()
            Toast.makeText(activity, getString(R.string.history_cleared), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent != null) {
            mListener = parent as Listener
        } else {
            mListener = context as Listener?
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onHistoryItemClicked(resultText: String)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_history_action_list_dialog_item, parent, false)) {

        internal val rowLayout: LinearLayout = itemView.findViewById<View>(R.id.row) as LinearLayout
        internal val actionTextView: TextView = itemView.findViewById<View>(R.id.action) as TextView
        internal val resultTextView: TextView = itemView.findViewById<View>(R.id.result) as TextView

        init {
            rowLayout.setOnClickListener {
                if (mListener != null) {
                    // Operator with !! allows to bypass nullability checking by the Kotlin compiler.
                    mListener!!.onHistoryItemClicked(resultTextView.text.toString())
                    dismiss()
                }
            }
        }

    }

    private inner class ItemAdapter internal constructor(private val mHistoryActionList: ArrayList<String>) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Regular expression lookbehind with delimiter "="
            val reg = Regex("(?<=[=])")
            val historyActionList = mHistoryActionList.get(position).split(reg)
            holder.actionTextView.text = if (historyActionList.size == 1) "" else historyActionList.first()
            holder.resultTextView.text = historyActionList.last().trim()
        }

        override fun getItemCount(): Int {
            return mHistoryActionList.count()
        }

    }

    companion object {

        private val ARG_HISTORY_ACTION = "history_action"

        fun newInstance(historyActionList: ArrayList<String>): HistoryActionListDialogFragment {
            val fragment = HistoryActionListDialogFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_HISTORY_ACTION, historyActionList)
            fragment.arguments = args
            return fragment
        }
    }

}