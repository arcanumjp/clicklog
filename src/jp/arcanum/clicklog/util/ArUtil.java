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
 * ユーティリティクラス
 *
 */
public class ArUtil {


    /**
     * プロパティ値取得
     * @param con コンテキスト
     * @param path パス
     * @param key キー名
     * @return キーに対応したプロパティが返る
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
     * ファイル読み込み処理
     * @param con コンテキスト
     * @param path パス
     * @return 読み込んだファイルをリストにして返す
     */
    public static List readFile(ServletContext con, final String path){

        //　戻り値の初期化
        List ret = new ArrayList() ;

        FileInputStream fis = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(con.getRealPath(path));
            ir  = new InputStreamReader(fis,"Shift_JIS");
            br  = new BufferedReader(ir);

            //　ファイルの全行を読み込み、戻り値に追加
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

        //　戻り値を返し、処理を終了する
        return ret;

    }

    /**
     * ファイル書き込み
     * @param con コンテキスト
     * @param path パス
     * @param lines 内容
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
