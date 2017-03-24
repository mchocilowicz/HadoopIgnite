package com.passwordentropy.mapreduce.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

/*
 * This class is calculating password strength by accepting password.
 * 
 * Our Requirements:::
 * We have two sets of rules:
 * Set 1 is manadatory.
   Set 1: Minimum 8 characters in length
   Set 2: Contains 3/4 of the following items:
 	1) Uppercase Letters
 	2) Lowercase Letters
 	3) Numbers
 	4) Symbols
 
 	Algorhytm:
 	We have two main methods:
 	 1) additions which will add scores on the basis of rules defined below.
 	 2) deductions which will deduct scores on the basis of rules defined below.
 	 
 	additions method is calling the following methods to add score
 	
 	1) length(password)
 		This will add score by (length of password*4)
 		
	2) upperCaseChracters(password)
		This will add score by ((length_of_password - no_of_uppercase_characters_in_password)*2)
	
	3) lowerCaseChracters(password)
		This will add score by ((length_of_password - no_of_lowercase_characters_in_password)*2)
		
	4) numbersLength(password)
		This will add score by (num_of_digits_in_password*4)
		
	5) symbolsLength(password)
		This will add score by (num_of_symbols_in_password*4)
		
	6) middleNumbersOrSymbols(password):
		This will add score by (num_of_digits_or_symbols_except_at_start_or_end_in_password*2)
	
	7) requirements(password)
		This will add score by using this method.
		a) Set 1 rule i.e. Minimum 8 characters condition is must.Then only this will add score.
		b) Set 2 should have atleast 3 rules satisfied.
		Ex: Ahdjd3783
		int rulesSatisfied=0;
		1) length >8 .So rulesSatisfied=1; 
		2) Has Uppercase character. True. So rulesSatisfied=2 i.e. increment by 1
		3) Has Lowercase character. True. So rulesSatisfied=3 i.e. increment by 1
		4) Has Digit. True. So rulesSatisfied=4 i.e. increment by 1
		5) Has Symbol. False. So rulesSatisfied=4 i.e. no increment  
		So the formula is score=score+(rulesSatisfied*2)
		
	
	deductions method is calling the following methods to deduct score
 	
 	1) hasLettersOnly(password)
 		This will reduce score by (num_of_letters) in password and this condition will work if password
 		is containing only letters.
 		
	2) hasDigitsOnly(password)
		This will reduce score by (num_of_digits) in password and this condition will work if password
 		is containing only digits.
 		
	3) consecutiveUpperCase(password)
		This will reduce score by (num_of_consecutive_upper_case_letters*2) in password.
		Ex: HDJDKsii
		
	4) consecutiveLowerCase(password)
		This will reduce score by (num_of_consecutive_lower_case_letters*2) in password.
		Ex: sjhdjh87
			
	5) consecutiveDigitCase(password)
		This will reduce score by (num_of_consecutive_digits*2) in password.
		Ex: sjhdjh87
		
	6) sequentialLetters(password)
		This will reduce score by (num_of_sequential_letters*3) in password.
		This will only if password has 3 or 3+ sequential characters
		Ex: ABCDEash
		
	7) sequentialNumbers(password)
		This will reduce score by (num_of_sequential_numbers*3) in password.
		This will only if password has 3 or 3+ sequential numbers
		Ex: 12345ash
		
	8) sequentialSymbols(password)
		This will reduce score by (num_of_sequential_symbols*3) in password.
		This will only if password has 3 or 3+ sequential symbols in our keyboard.
		Ex: !@#$%^ash
 * */

	
@Service
public class PasswordEntropyUtils {
	// This is the final Score or strength of our password.
	private long score = 0;

	private int MIN_PASSWORD_LENGTH = 8;
	// We need to reduce score for sequential characters if no of sequential
	// chracters are atleast 3.

	private int MIN_SEQUENTIAL_CHRACTERS = 3;

	// Regex pattern for checking only alphabets
	private final String ONLY_ALPHABETS = "[a-zA-Z]+";
	// Regex pattern for checking only digits
	private final String ONLY_DIGITS = "[0-9]+";
	
	String[][] customSeqSymbols = { { "!", "1" }, { "@", "2" }, { "#", "3" }, { "$", "4" }, { "%", "5" }, { "^", "6" },
			{ "&", "7" }, { "*", "8" }, { "(", "9" }, { ")", "10" }, { "-", "11" }, { "+", "12" } };

	// This will hold the only alphabetic pattern after compiling the above
	// ONLY_ALPHABETS pattern
	private Pattern onlyAlphabeticPattern;
	// This will hold the only digits pattern after compiling the above
	// ONLY_DIGITS pattern
	private Pattern onlyDigitsPattern;
	// This matcher object will be used to match our pattern with passwords.
	private Matcher matcher;

	public PasswordEntropyUtils() {
		onlyAlphabeticPattern = Pattern.compile(ONLY_ALPHABETS);
		onlyDigitsPattern = Pattern.compile(ONLY_DIGITS);
	}
	
	/*
	 * This method will be used by other classes to calculate strength
	 * 
	 * @param password
	 */
	public void calculateStrength(String password) throws NullPointerException {
		score = 0;
		additions(password);
		deductions(password);
		if (score > 100) {
			score = 100;
		}
	}


	/*
	 * This method will return score on the basis of algorhytm
	 */
	public long getScore() {
		return score;
	}

	/*
	 * This will add score on the basis of our defined rules
	 * 
	 * @param password
	 */
	protected void additions(String password) throws NullPointerException {
		length(password);
		upperCaseChracters(password);
		lowerCaseChracters(password);
		numbersLength(password);
		symbolsLength(password);
		middleNumbersOrSymbols(password);
		requirements(password);

	}

	/*
	 * This will reduce score on the basis of our defined rules
	 * 
	 * @param password
	 */
	protected void deductions(String password) throws NullPointerException {
		hasLettersOnly(password);
		hasDigitsOnly(password);
		consecutiveUpperCase(password);
		consecutiveLowerCase(password);
		consecutiveDigitCase(password);
		sequentialLetters(password);
		sequentialNumbers(password);
		sequentialSymbols(password);
	}

	/*
	 * This method is checking if password is containing letters only.
	 * 
	 * @param password
	 */
	protected void hasLettersOnly(String password) throws NullPointerException {
		matcher = onlyAlphabeticPattern.matcher(password);
		if (matcher.matches()) {
			score -= password.length();
		}
	}

	/*
	 * This method is checking if password is containing digits only.
	 * 
	 * @param password
	 */
	protected void hasDigitsOnly(String password) throws NullPointerException {
		matcher = onlyDigitsPattern.matcher(password);
		if (matcher.matches()) {
			score -= password.length();
		}
	}


	/*
	 * This method is checking if password has consecutive uppercase letters.
	 * Ex- DEFG , IUYUISS etc.
	 * 
	 * @param password
	 */
	protected void consecutiveUpperCase(String password) {
		int tempConsecutiveUpperCase = 0;
		int consecutiveUpperCase = 0;
		for (int index = 0; index < password.length(); index++) {
			if (Character.isUpperCase(password.charAt(index))) {
				tempConsecutiveUpperCase++;
			} else {
				if (tempConsecutiveUpperCase > 1) {
					consecutiveUpperCase += (tempConsecutiveUpperCase - 1);
				}
				tempConsecutiveUpperCase = 0;
			}
		}
		if (tempConsecutiveUpperCase > 1) {
			consecutiveUpperCase += (tempConsecutiveUpperCase - 1);
		}
		if (consecutiveUpperCase > 1) {
			score -= ((consecutiveUpperCase) * 2);
		}
	}

	/*
	 * This method is checking if password has consecutive lowercase letters.
	 * Ex- adhjdj123 , kksdjjkjd134 etc.
	 * 
	 * @param password
	 */
	protected void consecutiveLowerCase(String password) {
		int tempConsecutiveLowerCase = 0;
		int consecutiveLowerCase = 0;
		for (int index = 0; index < password.length(); index++) {
			if (Character.isLowerCase(password.charAt(index))) {
				tempConsecutiveLowerCase++;
			} else {
				if (tempConsecutiveLowerCase > 1) {
					consecutiveLowerCase += (tempConsecutiveLowerCase - 1);
				}
				tempConsecutiveLowerCase = 0;
			}
		}
		if (tempConsecutiveLowerCase > 1) {
			consecutiveLowerCase += (tempConsecutiveLowerCase - 1);
		}

		if (consecutiveLowerCase > 1) {
			score -= ((consecutiveLowerCase) * 2);
		}
	}

	/*
	 * This method is checking if password has consecutive digits. Ex-
	 * Yte6327683 , Ehd77637879 etc.
	 * 
	 * @param password
	 */
	protected void consecutiveDigitCase(String password) {
		int tempConsecutiveDigitCase = 0;
		int consecutiveDigitCase = 0;
		for (int index = 0; index < password.length(); index++) {
			if (Character.isDigit(password.charAt(index))) {
				tempConsecutiveDigitCase++;
			} else {
				if (tempConsecutiveDigitCase > 1) {
					consecutiveDigitCase += (tempConsecutiveDigitCase - 1);
				}
				tempConsecutiveDigitCase = 0;
			}
		}
		if (tempConsecutiveDigitCase > 1) {
			consecutiveDigitCase += (tempConsecutiveDigitCase - 1);
		}

		if (consecutiveDigitCase > 1) {
			score -= ((consecutiveDigitCase) * 2);
		}
	}

	/*
	 * This method is checking if password has sequential digits. Ex- Yte1234567
	 * , Ehd456789 etc.
	 * 
	 * @param password
	 */
	protected void sequentialNumbers(String password) {
		List<Integer> tempConsecutiveDigits = new ArrayList<Integer>();
		int seqNumbers = 0;
		for (int index = 0; index < password.length(); index++) {
			if (Character.isDigit(password.charAt(index))) {
				tempConsecutiveDigits.add(Integer.parseInt(password.charAt(index) + ""));
			} else {
				int newScore = findTotalSequentialNumbers(tempConsecutiveDigits);
				if (newScore >= 1) {
					seqNumbers += newScore;
				}
				tempConsecutiveDigits = new ArrayList<Integer>();
			}
		}
		if (tempConsecutiveDigits.size() >= 3) {
			int newScore = findTotalSequentialNumbers(tempConsecutiveDigits);
			if (newScore >= 1) {
				seqNumbers += newScore;
			}
		}
		if (seqNumbers >= (MIN_SEQUENTIAL_CHRACTERS - 2)) {
			score -= ((seqNumbers) * 3);
		}
	}

	/*
	 * This method is checking if password has sequntial letters. Here we check
	 * both sequential uppercase and sequential lowercase letters. Score will be
	 * calculated on the basis no of sequential uppercase letters and no of
	 * sequential lowercase if seq_upp_case_letters > seq_low_case_letters then
	 * seq_upp_case_letters is used to calculate score. Ex- ABCD2164 , DEFGR4157
	 * etc.
	 * 
	 * @param password
	 */
	protected void sequentialLetters(String password) {

		List<Integer> tempConsecutiveUpperCase = new ArrayList<Integer>();

		List<Integer> tempConsecutiveLowerCase = new ArrayList<Integer>();

		int seqUpperCase = 0;

		int seqLowerCase = 0;

		for (int index = 0; index < password.length(); index++) {
			if (Character.isUpperCase(password.charAt(index))) {
				tempConsecutiveUpperCase.add((int) password.charAt(index));
				if (tempConsecutiveUpperCase.size() >= 3) {
					int newScore = findTotalSequentialLettersSymbols(tempConsecutiveUpperCase);
					if (newScore > seqUpperCase) {
						seqUpperCase = newScore;
					}
				}
			} else {
				tempConsecutiveUpperCase = new ArrayList<Integer>();
			}

			if (Character.isLowerCase(password.charAt(index))) {
				tempConsecutiveLowerCase.add((int) password.charAt(index));
				if (tempConsecutiveLowerCase.size() >= 3) {
					int newScore = findTotalSequentialLettersSymbols(tempConsecutiveLowerCase);
					if (newScore > seqLowerCase) {
						seqLowerCase = newScore;
					}
				}
			} else {
				tempConsecutiveLowerCase = new ArrayList<Integer>();
			}
		}
		if (seqLowerCase >= (MIN_SEQUENTIAL_CHRACTERS - 1) || seqUpperCase >= (MIN_SEQUENTIAL_CHRACTERS - 1)) {
			int finalSeqNumbers = seqLowerCase > seqUpperCase ? seqLowerCase : seqUpperCase;
			score -= ((finalSeqNumbers - 1) * 3);
		}
	}

	/*
	 * This method is checking if password has sequntial symbols. Like on our
	 * keyboard,we have !@#$%^&*()-+ in sequence. Ex- Yte!@#$ , Ehd^&*( etc.
	 * 
	 * @param password
	 */
	protected void sequentialSymbols(String password) {
		List<Integer> tempConsecutiveSymbols = new ArrayList<Integer>();
		int seqSymbols = 0;
		for (int index = 0; index < password.length(); index++) {
			if (isSymbol(password.charAt(index))) {
				tempConsecutiveSymbols.add(findNumericValueOfSymbol(password.charAt(index)));

			} else {
				int newScore = findTotalSequentialNumbers(tempConsecutiveSymbols);
				if (newScore > seqSymbols) {
					seqSymbols += newScore;
				}
				tempConsecutiveSymbols = new ArrayList<Integer>();
			}
		}
		if (tempConsecutiveSymbols.size() >= 3) {
			int newScore = findTotalSequentialNumbers(tempConsecutiveSymbols);
			if (newScore > seqSymbols) {
				seqSymbols += newScore;
			}
		}
		if (seqSymbols >= (MIN_SEQUENTIAL_CHRACTERS - 2)) {
			score -= ((seqSymbols) * 3);
		}
	}


	/*
	 * This method is used to find numeric value of symbol from our custom dictionary.
	 * @param ch 
	 *  -single character representing a symbol.
	 */
	protected int findNumericValueOfSymbol(char ch) {
		for (int index = 0; index < customSeqSymbols.length; index++) {
			if ((ch + "").equals(customSeqSymbols[index][0])) {
				return Integer.parseInt(customSeqSymbols[index][1]);
			}
		}
		return 0;
	}

	/*
	 * This method is used to find total sequential letters or symbols.
	 * @param list 
	 *  -passing all sequential letters or symbols that exists in password
	 */
	protected int findTotalSequentialLettersSymbols(List<Integer> list) {
		int tempSeqNumbers = 0;
		int seqNumbers = 0;

		for (int index = 0; index < list.size() - 1; index++) {
			if (list.get(index + 1) - list.get(index) == 1) {
				tempSeqNumbers++;
			} else {
				if (seqNumbers == 0 || tempSeqNumbers > seqNumbers) {
					seqNumbers = tempSeqNumbers;
				}
				tempSeqNumbers = 0;
			}
		}
		if (seqNumbers == 0 || tempSeqNumbers > seqNumbers) {
			seqNumbers = tempSeqNumbers;
		}
		return seqNumbers;
	}

	/*
	 * This method is used to find total sequential numbers.
	 * @param list 
	 *  -passing all sequential numbers that exists in password
	 */
	protected int findTotalSequentialNumbers(List<Integer> list) {
		int tempSeqNumbers = 0;
		int finalSeqNumbers = 0;
		for (int index = 0; index < list.size() - 1; index++) {
			if (list.get(index + 1) - list.get(index) == 1) {
				tempSeqNumbers++;
			} else {
				if (tempSeqNumbers >= 2) {
					finalSeqNumbers += (tempSeqNumbers - 1);
				}
				tempSeqNumbers = 0;
			}
		}
		if (finalSeqNumbers == 0 || tempSeqNumbers > 0) {
			finalSeqNumbers += (tempSeqNumbers - 1);
		}
		return finalSeqNumbers;
	}

	/*
	 * This method is used to calculate score on the basis of length.
	 * @param password 
	 */
	protected void length(String password) throws NullPointerException {
		score += (password.length() * 4);
	}
	/*
	 * This method is used to calculate score on the basis of upper case characters contained in password.
	 * @param password 
	 */
	protected void upperCaseChracters(String password) throws NullPointerException {
		int _LEN_UPPER_CASE_CHRACTERS = 0;
		for (int index = 0; index < password.length(); index++) {
			if (Character.isUpperCase(password.charAt(index))) {
				_LEN_UPPER_CASE_CHRACTERS++;
			}
		}
		if (_LEN_UPPER_CASE_CHRACTERS > 0) {
			score += ((password.length() - _LEN_UPPER_CASE_CHRACTERS) * 2);
		}
	}

	/*
	 * This method is used to calculate score on the basis of lower case characters contained in password.
	 * @param password 
	 */
	protected void lowerCaseChracters(String password) throws NullPointerException {
		int _LEN_LOWER_CASE_CHRACTERS = 0;
		for (int index = 0; index < password.length(); index++) {
			if (Character.isLowerCase(password.charAt(index))) {
				_LEN_LOWER_CASE_CHRACTERS++;
			}
		}
		if (_LEN_LOWER_CASE_CHRACTERS > 0) {
			score += ((password.length() - _LEN_LOWER_CASE_CHRACTERS) * 2);
		}
	}

	/*
	 * This method is used to calculate score on the basis of no of digits contained in password.
	 * @param password 
	 */
	protected void numbersLength(String password) throws NullPointerException {
		int _LEN_NUMERIC_CHRACTERS = 0;
		for (int index = 0; index < password.length(); index++) {
			if (Character.isDigit(password.charAt(index))) {
				_LEN_NUMERIC_CHRACTERS++;
			}
		}
		if (_LEN_NUMERIC_CHRACTERS > 0 && !isNumbersOnly(password)) {
			score += (_LEN_NUMERIC_CHRACTERS * 4);
		}
	}

	/*
	 * This method is used to calculate score on the basis of no of symbols contained in password.
	 * @param password 
	 */
	protected void symbolsLength(String password) throws NullPointerException {
		int _LEN_SYMBOLS_CHRACTERS = 0;
		for (int index = 0; index < password.length(); index++) {
			if (!Character.isAlphabetic(password.charAt(index)) && !Character.isDigit(password.charAt(index))) {
				_LEN_SYMBOLS_CHRACTERS++;
			}
		}
		if (_LEN_SYMBOLS_CHRACTERS > 0) {
			score += (_LEN_SYMBOLS_CHRACTERS * 6);
		}
	}

	/*
	 * This method is used to calculate score on the basis of no of digits or symbols contained in password except start or end position.
	 * Ex- A2767&*(hgf
	 * @param password 
	 */
	protected void middleNumbersOrSymbols(String password) throws NullPointerException {
		int _LEN_MIDDLE_NUMBERS_OR_SYMBOLS_CHRACTERS = 0;
		for (int index = 1; index < password.length() - 1; index++) {
			if (Character.isDigit(password.charAt(index)) || (isSymbol(password.charAt(index)))) {
				_LEN_MIDDLE_NUMBERS_OR_SYMBOLS_CHRACTERS++;
			}
		}
		if (_LEN_MIDDLE_NUMBERS_OR_SYMBOLS_CHRACTERS > 0) {
			score += (_LEN_MIDDLE_NUMBERS_OR_SYMBOLS_CHRACTERS * 2);
		}
	}

	/*
	 * This method will calculate the score on the basis of minimum requirements we required.
	 * @param password
	 * */
	protected void requirements(String password) throws NullPointerException {
		boolean hasValidLength = false, hasUpperCase = false, hasLowerCase = false, hasNumber = false,
				hasSymbol = false;
		if (password.length() >= MIN_PASSWORD_LENGTH) {
			hasValidLength = true;
		}
		int rulesCount = 0;
		for (int index = 0; index < password.length(); index++) {
			if (!hasNumber && Character.isDigit(password.charAt(index))) {
				hasNumber = true;
				rulesCount++;
			}
			if (!hasUpperCase && Character.isUpperCase(password.charAt(index))) {
				hasUpperCase = true;
				rulesCount++;
			}
			if (!hasLowerCase && Character.isLowerCase(password.charAt(index))) {
				hasLowerCase = true;
				rulesCount++;
			}
			if (!hasSymbol && isSymbol(password.charAt(index))) {
				hasSymbol = true;
				rulesCount++;
			}
		}
		if (hasValidLength && rulesCount >= 3) {
			score += ((rulesCount + 1) * 2);
		}
	}

	/*
	 * This method will be used to check whether character is symbol or not.
	 * @param password
	 * */
	protected static boolean isSymbol(char chracter) throws NullPointerException {
		if (!Character.isAlphabetic(chracter) && !Character.isDigit(chracter)) {
			return true;
		}
		return false;
	}
	/*
	 * This method will be used to check whether password is carrying digits only.
	 * Ex- 1265656125,3878379,etc.
	 * @param password
	 * */
	protected static boolean isNumbersOnly(String password) throws NullPointerException {
		boolean isDigitOnly = true;
		for (int index = 0; index < password.length(); index++) {
			if (!Character.isDigit(password.charAt(index))) {
				isDigitOnly = false;
				break;
			}
		}
		return isDigitOnly;
	}
}
