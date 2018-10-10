package org.tinos.engine.imp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.tinos.engine.CogsBinaryForestAnalyzer;
import org.tinos.fhmm.FHMMList;
import org.tinos.fhmm.NeroFeedHMM;
import org.tinos.fhmm.imp.FMHMMListImp;
import org.tinos.fhmm.imp.NeroFeedHMMImp;
import org.tinos.utils.EngineUtils;
import org.tinos.utils.imp.EngineUtilsImp;
import org.tinos.zabbi.DataString;
public class CogsBinaryForestAnalyzerImp implements  CogsBinaryForestAnalyzer{
	private FHMMList fHMMList;
	private NeroFeedHMM neroFeedHMM;
	private EngineUtils engineUtils;
	public void init() throws IOException {
		this.fHMMList = new FMHMMListImp();
		fHMMList.index();
		neroFeedHMM = new NeroFeedHMMImp(); 
		engineUtils = new EngineUtilsImp();
	}
	
	@SuppressWarnings(DataString.RAW_TYPES)
	public List<String> parserString(String inputString) {
		Map <String, String> wordsForest = fHMMList.getWords();
		List<String> outputString = new ArrayList<>();
		Map <Integer, Map> forestRoots = fHMMList.getRoot();
		int inputStringLength = inputString.length();
		int forestDepth = DataString.INT_ZERO;
		int countInputStringLength;
		for(int charPosition = DataString.INT_ZERO; charPosition < inputStringLength; charPosition 
				+= (countInputStringLength == DataString.INT_ZERO ? DataString.INT_ONE 
						: countInputStringLength)){
			String countWordNode = DataString.EMPTY_STRING + inputString.charAt(charPosition);
			countWordNode = neroFeedHMM.getBinaryForestRecurWord(countWordNode, inputString
					, charPosition, inputStringLength, forestRoots
					, forestDepth);
			int countWordNodeLength = countWordNode.length();
			countInputStringLength = countWordNodeLength;
			if(countWordNode.length() == DataString.INT_ONE){
				outputString.add(countWordNode);
			}else if(countWordNode.length() == DataString.INT_TWO){
				countInputStringLength = engineUtils.doSlangPartCheck(countInputStringLength
						,outputString, countWordNode, wordsForest);	 
			}else if(countWordNode.length() == DataString.INT_THREE) {
				countInputStringLength = engineUtils.doPOSAndEMMCheck(countInputStringLength
						,outputString, wordsForest, countWordNode);	 
			}else if(countWordNode.length() == DataString.INT_FOUR){
				countInputStringLength = engineUtils.doSlangCheck(countInputStringLength
						,outputString, countWordNode, wordsForest);
			}		
		}
		return outputString;
	}

	public Map<String, String> getWord() throws IOException {
		return fHMMList.getWords();
	}
}

 
