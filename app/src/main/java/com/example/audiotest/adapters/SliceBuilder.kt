package com.example.audiotest.adapters

import android.content.Context
import android.util.Log
import com.example.audiotest.TAG
import com.example.audiotest.models.Slize

class SliceBuilder() {

    //Skapar och skickar tillbaka en lista av sliceobjekt med olika startpunkter
    fun makeSlices(duration: Int, level: Int): List<Slize> {


        //lista av sliceobjekt initialiseras
        val sliceList = mutableListOf<Slize>()

        var slizeDivisions : Int

        when (level) {
            0 -> slizeDivisions = 4
            1 -> slizeDivisions = 5
            2 -> slizeDivisions = 6
            3 -> slizeDivisions = 7
            4 -> slizeDivisions = 9
            5 -> slizeDivisions = 10
            6 -> slizeDivisions = 11
            else -> slizeDivisions = 4
        }

        val randomColors = SlizeColors.colors.shuffled().take(slizeDivisions)

        val sliceLength = (duration / slizeDivisions)

        for (number in 1..slizeDivisions) {
            sliceList.add(
                Slize(
                    number,
                    (number - 1) * sliceLength,
                    sliceLength.toLong(),
                    randomColors[number - 1]
                )
            )
        }

        Log.d(TAG, "made ${sliceList.size} slices with the length of $sliceLength each")

        return superShuffle(sliceList)
    }


    //Algoritm för att blanda slicelista, men att ingen slice hamnar i turordning efter varandra
    fun superShuffle(list: MutableList<Slize>): MutableList<Slize> {

        var superShuffled = false

        while (superShuffled == false) {
            superShuffled = true
            list.shuffle()
            for (i in 0..list.size) {
                if (i <= list.size - 2) {
                    if ((list[i].number + 1) == list[i + 1].number || list[0].number == 1) {
                        superShuffled = false
                    }
                }
            }
        }
        Log.d("kolla", "Made ${list.size} slizes in the following order : ${list}")
        return list
    }
}


object SlizeColors {

val colors = listOf(
    "#00FFB5",
    "#59B2E4",
    "#735CF4",
    "#B45CF4",
    "#E20064",
    "#FF1D00",
    "#FF8B71",
    "#F2FF9A",
    "#FCE42B",
    "#A0C000",
    "#53C275",
    "#009A16"
)

}



