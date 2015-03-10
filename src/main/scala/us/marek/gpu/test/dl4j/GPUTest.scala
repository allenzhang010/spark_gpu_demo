package us.marek.gpu.test.dl4j

import jcuda.runtime.JCuda
import org.apache.spark.{SparkContext, SparkConf}

import jcuda._
import jcuda.jcublas._
import JCublas.{ cublasAlloc, cublasCsscal, cublasGetVector, cublasSetVector }

import scala.util.Random


object GPUTest extends App {

  val conf = new SparkConf().setMaster("local[2]").setAppName("gpuTest")
  val sc = new SparkContext(conf)

  val nRows = 10
  val numEl = 100
  val rand = new Random()
  // 10-element vectors
  val randInput = Array.fill(nRows, numEl)(rand.nextFloat)
  val rdd = sc.parallelize(randInput)

  def multOnGpu(vector: Array[Float], scalar: Float): Array[Float] = {
    val srcPtr = new Pointer()
    val numEl = vector.length
    cublasAlloc(numEl, Sizeof.FLOAT, srcPtr)
    cublasSetVector(numEl, Sizeof.FLOAT, Pointer.to(vector), 1, srcPtr, 1)
    cublasCsscal(numEl, scalar, srcPtr, 1)
    val output = Array.fill(numEl)(0.0F)
    cublasGetVector(numEl, Sizeof.FLOAT, srcPtr, 1, Pointer.to(output), 1)
    output
  }

  val mapped = rdd.map(row => multOnGpu(row, 100.0F))
  mapped.collect.map(_.toSeq).foreach(println)

  sc.stop()
}