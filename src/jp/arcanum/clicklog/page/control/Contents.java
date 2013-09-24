package jp.arcanum.clicklog.page.control;


import java.util.List;

import jp.arcanum.clicklog.util.ArUtil;
import net.sf.click.control.ActionLink;

/**
 * �R���e���c��\������N���X
 *
 */
public class Contents extends ActionLink{

    /**
     * ���X�g�`���̏ꍇ�̕\�����ǂ���
     */
    private boolean islist = false;

    /**
     * �R���X�g���N�^
     * @param yyyymmdd �\���������N�����AHTML���name�����ɂȂ�
     * @param flg ���X�g�t���O
     */
    public Contents(String yyyymmdd,boolean flg){
        super(yyyymmdd + flg);
        this.islist = flg;
    }


    /**
     * �����񉻁B<br>
     */
    public String toString(){

        String path = "/WEB-INF/data/blog/";
        path = path + this.name.substring(0,8) + ".txt";
        List lines = ArUtil.readFile(super.getContext().getServletContext(), path);

        String ymd = this.name.substring(0,4) + "/" +
                     this.name.substring(4,6) + "/" +
                     this.name.substring(6,8) + " �̓��L";

        //�@���X�g�`���̕\���̏ꍇ
        if(this.islist){

            super.setLabel("<small>" + lines.get(0) + "</small>");
            return "<small>" + ymd + "</small><br> &nbsp;&nbsp;" +  super.toString();

        }
        else{

            super.setLabel("������ǂ�");

            String contents = ymd  +"<br>";
            contents = contents + "�^�C�g���F" + lines.get(0) + "<br>";

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
