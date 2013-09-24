package jp.arcanum.clicklog.page.control;


import java.util.List;

import jp.arcanum.clicklog.util.ArUtil;
import net.sf.click.control.ActionLink;

/**
 * コンテンツを表現するクラス
 *
 */
public class Contents extends ActionLink{

    /**
     * リスト形式の場合の表示かどうか
     */
    private boolean islist = false;

    /**
     * コンストラクタ
     * @param yyyymmdd 表示したい年月日、HTML上でname属性になる
     * @param flg リストフラグ
     */
    public Contents(String yyyymmdd,boolean flg){
        super(yyyymmdd + flg);
        this.islist = flg;
    }


    /**
     * 文字列化。<br>
     */
    public String toString(){

        String path = "/WEB-INF/data/blog/";
        path = path + this.name.substring(0,8) + ".txt";
        List lines = ArUtil.readFile(super.getContext().getServletContext(), path);

        String ymd = this.name.substring(0,4) + "/" +
                     this.name.substring(4,6) + "/" +
                     this.name.substring(6,8) + " の日記";

        //　リスト形式の表示の場合
        if(this.islist){

            super.setLabel("<small>" + lines.get(0) + "</small>");
            return "<small>" + ymd + "</small><br> &nbsp;&nbsp;" +  super.toString();

        }
        else{

            super.setLabel("続きを読む");

            String contents = ymd  +"<br>";
            contents = contents + "タイトル：" + lines.get(0) + "<br>";

            for(int i = 1 ; i < 4 ; i++){

                if(lines.size()<=i){
                    break;
                }

                contents = contents + lines.get(i) + "<br>";
            }

            return contents +  super.toString();
        }


    }

}
