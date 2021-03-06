package com.executor.crudapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.executor.crudapplication.db.UserEntity
import com.executor.crudapplication.db.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), UserAdapter.RowClickListener {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        tvCount.setText(mUserViewModel.)

        fabAddBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
            startActivity(intent)
        }

        val adapter = UserAdapter(this@MainActivity, this@MainActivity)
        rvUser.adapter = adapter
        rvUser.layoutManager = LinearLayoutManager(this@MainActivity)

        mUserViewModel = ViewModelProvider(this@MainActivity)[UserViewModel::class.java]

        mUserViewModel.getAllUser.observe(this@MainActivity) {
            adapter.setListData(it)
        }

    }

    override fun onDeleteUserClickListener(userEntity: UserEntity) {
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle("Are you Sure Delete this ${userEntity.fName} ?")
        dialog.setPositiveButton("Delete") { _, _ ->
            GlobalScope.launch {
                mUserViewModel.deleteUser(userEntity)
            }
            Toast.makeText(this, "${userEntity.fName} Delete", Toast.LENGTH_SHORT).show()
        }
        dialog.setNegativeButton("No") { _, _ ->
            Toast.makeText(this, "Not Delete", Toast.LENGTH_SHORT).show()
        }
        dialog.show()

    }

    override fun onItemClickListener(userEntity: UserEntity) {
        val intent = Intent(this@MainActivity, UpdateActivity::class.java)
//        intent.putExtra("User", userEntity[position])
        intent.putExtra("id", userEntity.id)
        intent.putExtra("fname", userEntity.fName)
        intent.putExtra("lname", userEntity.lName)
        intent.putExtra("email", userEntity.email)
        intent.putExtra("dob", userEntity.dob)
        intent.putExtra("number", userEntity.number)
        intent.putExtra("img", userEntity.image)
        startActivity(intent)
    }
}
