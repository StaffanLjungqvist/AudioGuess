package com.example.audiotest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.audiotest.adapters.AudioHelper
import com.example.audiotest.models.Phrase
import java.util.*
import androidx.core.view.isVisible
import com.example.audiotest.adapters.GameAdapter
import com.example.audiotest.adapters.PhraseLoader


val TAG = "felsök"


class MainActivity : AppCompatActivity() {

    private lateinit var myRecyclerView: RecyclerView
    private lateinit var myRecycleAdapter: MyRecyclerAdapter
    private lateinit var currentPhrase: Phrase
    private lateinit var audioHelper: AudioHelper
    private lateinit var gameAdapter : GameAdapter
    private var level = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        actionBar?.hide()

        gameAdapter = GameAdapter()

        //To do - safecast
        currentPhrase = PhraseLoader(this).loadPhrase(level)!!

        audioHelper = AudioHelper(this, currentPhrase.audioFile.file)

        //Setting up recycler view
        myRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        myRecycleAdapter = MyRecyclerAdapter(this, currentPhrase.slizes, audioHelper)
        myRecyclerView.adapter = myRecycleAdapter
        myRecyclerView.layoutManager = GridLayoutManager(this, currentPhrase.slizes.size)

        findViewById<TextView>(R.id.tvSentence).text = currentPhrase.text


        //Knappar

        findViewById<Button>(R.id.btnCheck).setOnClickListener {

            myRecycleAdapter.runLight(currentPhrase.slizes)

            if (checkIfCorrect()) {
                audioHelper.playAudio()
            } else {
                audioHelper.playSlices(currentPhrase.slizes)
            }
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            loadPhrase()
        }

        findViewById<Button>(R.id.btnSkip).setOnClickListener {
            loadPhrase()
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(myRecyclerView)


    }

    fun loadPhrase() {
        level = gameAdapter.advanceLevel()
        currentPhrase = PhraseLoader(this).loadPhrase(level)!!

    //To do, optimera release mediaplayer
        audioHelper = AudioHelper(this, currentPhrase.audioFile.file)

        myRecycleAdapter.slizes = currentPhrase.slizes
        myRecycleAdapter.audioHelper = audioHelper
        myRecyclerView.adapter = myRecycleAdapter
        myRecyclerView.layoutManager = GridLayoutManager(this, currentPhrase.slizes.size)


        findViewById<Button>(R.id.btnCheck).isVisible = true
        findViewById<Button>(R.id.btnNext).isVisible = false
        findViewById<TextView>(R.id.tvSentence).text = currentPhrase.text
    }


    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(

        ItemTouchHelper.LEFT.or(
            ItemTouchHelper.RIGHT
        ), 0
    ) {


        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var startPosition = viewHolder.adapterPosition
            var endPosition = target.adapterPosition


            Collections.swap(currentPhrase.slizes, startPosition, endPosition)


            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")

        }
    }


    fun checkIfCorrect(): Boolean {

        var sortedList = currentPhrase.slizes.sortedBy { it.number }
        Log.d("kolla", "listan i rätt ordning; ${sortedList}")

        if (sortedList.equals(currentPhrase.slizes)) {
            audioHelper.playSuccess()
            findViewById<TextView>(R.id.tvSentence).text = "That is correct!"
        //    findViewById<Button>(R.id.btnCheck).isVisible = false
            findViewById<Button>(R.id.btnNext).isVisible = true
            Log.d("kolla", "Listan är i rätt ordning!")
            return true
        }
        return false
    }


}