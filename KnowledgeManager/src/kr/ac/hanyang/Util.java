package kr.ac.hanyang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Util {

	public static String fileRead(String filename) {
		// 버퍼 생성
		BufferedReader br = null;

		// Input 스트림 생성
		InputStreamReader isr = null;

		// File Input 스트림 생성
		FileInputStream fis = null;

		// File 경로
		File file = new File(filename);

		// 버퍼로 읽어들일 임시 변수
		String temp = "";
		// 최종 내용 출력을 위한 변수
		StringBuffer content = new StringBuffer();

		try {

			// 파일을 읽어들여 File Input 스트림 객체 생성
			fis = new FileInputStream(file);

			// File Input 스트림 객체를 이용해 Input 스트림 객체를 생서하는데 인코딩을 UTF-8로 지정
			isr = new InputStreamReader(fis, "UTF-8");

			// Input 스트림 객체를 이용하여 버퍼를 생성
			br = new BufferedReader(isr);

			// 버퍼를 한줄한줄 읽어들여 내용 추출
			while ((temp = br.readLine()) != null) {
				content.append(temp);
				content.append("\n");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return content.toString();
	}
	
	/**
	 * OWL 파일 생성을 위한 메소드
	 * 
	 * @param filename
	 *            생성 파일 이름
	 * @param string
	 *            파일내용
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void FileOutput(String filename, String string) throws FileNotFoundException, IOException {
		FileWriter fw = new FileWriter(filename);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(string + "\r\n");

		bw.close();
		fw.close();
	}
}
