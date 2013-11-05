package org.concordance

import java.io.FileInputStream
import com.googlecode.clearnlp.engine.EngineGetter
import com.googlecode.clearnlp.reader.AbstractReader
import com.googlecode.clearnlp.util.UTInput
import scala.collection.JavaConversions._
//import scala.collection.JavaConverters._
import collection.immutable.SortedMap

object Concordance {

  val concordance = scala.collection.mutable.HashMap.empty[String,(Int,List[Int])]

  def addToConcordance(wordIn:String,sentenceNumIn:Int) {
    //make alll words lower case
    val word = wordIn.toLowerCase
    val sentenceNum = sentenceNumIn + 1
    if (concordance.contains(word)) 
    {
      //if key already exists, update frequency to +=1 and add the sentence number to the list
      concordance.update(word,(concordance(word)._1 + 1, concordance(word)._2 :+ sentenceNum))
    }
    else
    {
      //else add key to table and create list with first element being the current sentence number
      concordance(word) = (1,List(sentenceNum))
    }
  }

  def punctuationCleanser(sentences: java.util.List[java.util.List[String]]): scala.collection.mutable.Buffer[scala.collection.mutable.Buffer[String]] = {

    //clean all punctuation away from final tokenized collection
    sentences.map(i => i filter (_.matches("[a-zA-Z0-9].*")))
  }

  val language = AbstractReader.LANG_EN   
  val dictFile = "dictionary-1.4.0.zip"

  def main(args: Array[String]) {
    val text_file = args(0)
    //Create a new tokenizer pattern based on the supplied dictionary 
    val tokenizer  = EngineGetter.getTokenizer(language, new FileInputStream(dictFile))
    //Create a segmenter based on the english language
    val segmenter = EngineGetter.getSegmenter(language, tokenizer)
    //Open the input file
    val reader = UTInput.createBufferedFileReader(text_file)
    val sentences = segmenter.getSentences(reader)
    //Remove punctuation tokens
    val cleanSentences = punctuationCleanser(sentences)
    //Index each sentence
    val indexedSentences = cleanSentences.zipWithIndex
    //Add each word to concordance
    indexedSentences.map(i => i._1.map(word => addToConcordance(word,i._2)))
    //sort concordance
    val sortedConcordances = SortedMap(concordance.toSeq:_*)
    sortedConcordances.map(word => println(word))
  }
}

