package jp.arcanum.clicklog.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;


/**
 * ���[�e�B���e�B�N���X
 *
 */
public class ArUtil {


    /**
     * �v���p�e�B�l�擾
     * @param con �R���e�L�X�g
     * @param path �p�X
     * @param key �L�[��
     * @return �L�[�ɑΉ������v���p�e�B���Ԃ�
     */
    public static String getProperty(ServletContext con, String path,String key){


        String ret = "";

        Properties prop = new Properties();
        try {
            prop.load(con.getResourceAsStream(path + ".properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ret = prop.getProperty(key);

        return ret;

    }


    /**
     * �t�@�C���ǂݍ��ݏ���
     * @param con �R���e�L�X�g
     * @param path �p�X
     * @return �ǂݍ��񂾃t�@�C�������X�g�ɂ��ĕԂ�
     */
    public static List readFile(ServletContext con, final String path){

        //�@�߂�l�̏�����
        List ret = new ArrayList() ;

        FileInputStream fis = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(con.getRealPath(path));
            ir  = new InputStreamReader(fis,"Shift_JIS");
            br  = new BufferedReader(ir);

            //�@�t�@�C���̑S�s��ǂݍ��݁A�߂�l�ɒǉ�
            while(br.ready()){
                String line = br.readLine();
                ret.add(line);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally{
            try {
                br.close();
                ir.close();
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        //�@�߂�l��Ԃ��A�������I������
        return ret;

    }

    /**
     * �t�@�C����������
     * @param con �R���e�L�X�g
     * @param path �p�X
     * @param lines ���e
     */
    public static void writeFile(
            ServletContext con,
            String path ,
            List   lines){


        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try{

            fos = new FileOutputStream(con.getRealPath(path));
            osw = new OutputStreamWriter(fos,"Shift_JIS");
            bw  = new BufferedWriter(osw);

            bw.write("");

            for(int i = 0 ; i < lines.size() ; i++){
                if (((String)lines.get(i)).equals("\n")) {
                    bw.write((String)lines.get(i));
                } else {
                    bw.write((String)lines.get(i) + "\n");
                }
            }

        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        finally{
            try{
                bw.close();
                osw.close();
                fos.close();
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }


    }


}
