package com.baruckis.calculator

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 * HistoryActionListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
</pre> *
 *
 * You activity (or fragment) needs to implement [HistoryActionListDialogFragment.Listener].
 */
class HistoryActionListDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_history_action_list_dialog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val recyclerView = view!!.findViewById<View>(R.id.list) as RecyclerView?
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ItemAdapter(arguments.getInt(ARG_ITEM_COUNT))
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
        fun onItemClicked(position: Int)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_history_action_list_dialog_item, parent, false)) {

        internal val text: TextView

        init {
            text = itemView.findViewById<View>(R.id.text) as TextView
            text.setOnClickListener {
                if (mListener != null) {
                    mListener!!.onItemClicked(adapterPosition)
                    dismiss()
                }
            }
        }// TODO: Customize the item layout

    }

    private inner class ItemAdapter internal constructor(private val mItemCount: Int) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = position.toString()
        }

        override fun getItemCount(): Int {
            return mItemCount
        }

    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_ITEM_COUNT = "item_count"

        // TODO: Customize parameters
        fun newInstance(itemCount: Int): HistoryActionListDialogFragment {
            val fragment = HistoryActionListDialogFragment()
            val args = Bundle()
            args.putInt(ARG_ITEM_COUNT, itemCount)
            fragment.arguments = args
            return fragment
        }
    }

}
