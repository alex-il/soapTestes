package cap

import java.io.FileWriter

import scala.io.Codec.string2codec

import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients

object Cap12Tester {

  def main(args: Array[String]) {
//    println("Working ....")
    val date1 = "##Date##"
    val datePlus = "##Date@@##"
    val seq = "##NUM##"
    val dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
    val dateMilis = java.lang.System.currentTimeMillis + 45000
    def currentDirectory = new java.io.File( "." ).getCanonicalPath +"/bin/data/"

    val currDate = new java.text.SimpleDateFormat(dateFormat).format(new java.util.Date) + "+02:00"
    val currDatePlus = new java.text.SimpleDateFormat(dateFormat).format(new java.util.Date(dateMilis)) + "+02:00"
    val seqNum = scala.io.Source.fromFile(currentDirectory +"seq.xml").mkString.toInt + 1
    val seqNumStr = seqNum.toString()
    println( "currentDirectory:"+currentDirectory)
    val xmlIn = scala.xml.XML.loadFile(currentDirectory+"soap12.xml").mkString
    val soapXml = xmlIn.replaceAll("##Date##", currDate).replaceAll("##NUM##", seqNumStr).replaceAll(datePlus, currDatePlus)
//    println(soapXml)
    sendSoap12Cap12(soapXml)
    fileWriter( seqNumStr)
//    println("___end___")
  }

  def readLoadedMovies: Set[String] = {
    scala.io.Source.fromFile("d:/loadedMovies.txt")("UTF-8").getLines().toSet
  }

  /**
   *
   */
  def parser(xmlData: scala.xml.Elem) {
    var i = 1
    var title = ""
    (xmlData \\ "a").foreach { item =>
      title = item.text

      println(i + ". " + title)
      val uri = (item \ "@href")
    }
  }

  /**
   *
   */
  def fileWriter(seqNum: String) {
    val fw = new FileWriter("d:/Downloads/  seq.xml", false)
    println("seq:"+seqNum)
    fw.write(seqNum)
    fw.close()
  }

  /**
   *
   */
  def sendSoap12Cap12(soapXml: String) {
    val strURL = "http://Oleg-Lap:8888/mocknotify_Threat"
    val strSoapAction = "mocknotify_Threat"
    val httpPost = new HttpPost(strURL)
    val fileEntity = new org.apache.http.entity.StringEntity(soapXml)
    httpPost.setEntity(fileEntity)
    val httpClient = HttpClients.createDefault()
    val response = httpClient.execute(httpPost)
    val body = response.getEntity().getContent()
    
    println("----------response------------")
    println(scala.io.Source.fromInputStream(body).mkString)
    println("status code: " + response.getStatusLine().getStatusCode()
      + ", " + response.getStatusLine().getReasonPhrase())
  }

}