package com.example.socialfit.data

class UserAdapter(var items: List<UserData>) : androidx.recyclerview.widget.RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    var username: String? = null
    class UserViewHolder(val view: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val txtUser = view.findViewById<android.widget.TextView>(com.example.socialfit.R.id.txtUsername)
    }

    fun addUser(user: UserData) {
        val newItems = items.toMutableList()
        newItems.add(user)
        items = newItems.toList()
        notifyItemChanged(items.size - 1)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): UserViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(com.example.socialfit.R.layout.layout_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = items[position]
        holder.txtUser.text = "${item.name} (@${item.username})"
        holder.txtUser.setOnClickListener {
            val intent = android.content.Intent(holder.itemView.context, com.example.socialfit.ProfileActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("otherUsername", item.username)
            holder.itemView.context.startActivity(intent)
        }
    }
}