package org.neuroph.contrib.convolution.example;

import org.neuroph.contrib.convolution.ConvolutionLayer;
import org.neuroph.contrib.convolution.ConvolutionNeuralNetwork;
import org.neuroph.contrib.convolution.ConvolutionUtils;
import org.neuroph.contrib.convolution.InputMapsLayer;
import org.neuroph.contrib.convolution.Kernel;
import org.neuroph.contrib.convolution.Layer2D;
import org.neuroph.contrib.convolution.util.ModelMetric;
import org.neuroph.contrib.convolution.util.WeightVisualiser;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.NeuralNetworkFactory;

public class SimpleLearningExample {

	public static void testLearningOneLayer() {
		ConvolutionNeuralNetwork convolutionNet = new ConvolutionNeuralNetwork();
		convolutionNet.setLearningRule(new BackPropagation());

		// all this should go to ConvolutionNeuralNetwork class
		// just provide methods for network properties specification or
		// something...
		Kernel convolutionKernel = new Kernel(5, 5);

		// create input layer
		Layer2D.Dimension inputDimension = new Layer2D.Dimension(5, 5);
		InputMapsLayer inputLayer = new InputMapsLayer(inputDimension);
		// add input layer to network
		convolutionNet.addLayer(inputLayer);

		// create convolutional layer - use constructor instead of factory
		// method
		ConvolutionLayer convolutionLayer = new ConvolutionLayer(inputLayer, convolutionKernel);
		// create and add two feature maps to convolution layer
		Layer2D featureMap1 = new Layer2D(convolutionLayer.getDimension(), ConvolutionLayer.neuronProperties); // neuron
		// properties
		convolutionLayer.addFeatureMap(featureMap1);
		Layer2D featureMap2 = new Layer2D(convolutionLayer.getDimension(), ConvolutionLayer.neuronProperties); // neuron
		// properties
		convolutionLayer.addFeatureMap(featureMap2);

		// add convolution layer to network
		convolutionNet.addLayer(convolutionLayer);

		// connectFeatureMaps input and convolution layer
		ConvolutionUtils.connectFeatureMaps(inputLayer, convolutionLayer, 0, 0);
		ConvolutionUtils.connectFeatureMaps(inputLayer, convolutionLayer, 0, 1);

		NeuralNetworkFactory.setDefaultIO(convolutionNet);

		// CREATE DATA SET

		DataSet dataSet = new DataSet(25, 2);
		dataSet.addRow(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				new double[] { 1, 0 });
		dataSet.addRow(new double[] { 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 },
				new double[] { 0, 1 });

		// TRAIN NETWORK

		convolutionNet.getLearningRule().setMaxError(0.00001);
		convolutionNet.learn(dataSet);

		WeightVisualiser visualiser1 = new WeightVisualiser(convolutionLayer.getFeatureMap(0), convolutionKernel);
		visualiser1.displayWeights();

		WeightVisualiser visualiser2 = new WeightVisualiser(convolutionLayer.getFeatureMap(1), convolutionKernel);
		visualiser2.displayWeights();

		// CREATE TEST SET

		DataSet testSet = new DataSet(25, 2);
		testSet.addRow(new double[] { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				new double[] { 1, 0 });
		testSet.addRow(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
				new double[] { 1, 0 });
		testSet.addRow(new double[] { 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0 },
				new double[] { 0, 1 });

		// TEST NETWORK

		ModelMetric.calculateModelMetric(convolutionNet, testSet);

	}

	public static void main(String[] args) {
		testLearningOneLayer();
	}
}
