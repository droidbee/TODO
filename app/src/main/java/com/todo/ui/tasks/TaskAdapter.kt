package com.todo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.todo.databinding.RecyclerRowTaskBinding
import com.todo.db.TaskEntity

class TaskAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<TaskEntity, TaskAdapter.TaskViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(RecyclerRowTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task=getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(task)
        }
        holder.bind(task)
    }

    override fun getItemId(position: Int): Long {
        val task=getItem(position)
        return task.taskId.toLong()
    }
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [TaskEntity]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem.taskName == newItem.taskName
        }
    }


    /**
     * The TaskViewHolder constructor takes the binding variable from the associated
     * ListViewItem, which gives it access to the full [TaskEntity] information.
     */
    class TaskViewHolder(private var binding: RecyclerRowTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskEntity) {
            binding.task = task
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (task: TaskEntity) -> Unit) {
        fun onClick(task: TaskEntity) = clickListener(task)

    }
}

