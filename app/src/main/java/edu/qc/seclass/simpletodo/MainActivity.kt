package edu.qc.seclass.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()

    //initialize the var later
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that our data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }
        //Detect when the user clicks the addButton
        /*findViewById<Button>(R.id.addButton).setOnClickListener {
            //Code inside here will be executed after the user click the addButton

        }*/

        loadItems()

        //Look up recyclerView in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager =  LinearLayoutManager(this)


        //set up the button and input field, so that the user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField);
        //Get a reference to the button
        //and then set an onclicklistener
        findViewById<Button>(R.id.addButton).setOnClickListener {
            //1. Grab the text the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter the data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. Clear the keyboard text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //save the data that the user has inputed
    //save data by writing and reading from a file

    //Get the file we need
    fun getDataFile() : File{
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
    //Save items by writing them into our data file
    fun saveItems(){
        try{
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }
}