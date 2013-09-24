package jp.arcanum.clicklog.page;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import jp.arcanum.clicklog.util.ArUtil;
import net.sf.click.Page;
import net.sf.click.control.Submit;

/**
 * �u���O�\���y�[�W
 *
 */
public class ShowBlogPage extends AbstractPage{
    
    
    private String yyyymmdd = "";
    public  String showymd = "";
    
    public String blg_title = "";
    public String  blg_body  = "";
    
    private Submit back = new Submit("back","�߂�");
    
    private Submit editblog = new Submit("editblog","���̃u���O��ҏW����"){
        
        public String toString(){
            HttpSession sess = super.getContext().getSession();
            if(sess.getAttribute("LOGIN")==null){
                return "";
            }
            return super.toString();
        }
    
    };

    /**
     * �R���X�g���N�^
     *
     */
    public ShowBlogPage(){

        this.back.setListener(this, "clickBack");
        super.form.add(this.back);
    
    }
    
    
    /**
     * �N�����ݒ�
     * @param yyyymmdd
     */
    public void setDate(String yyyymmdd){
        this.yyyymmdd = yyyymmdd;
    }
    
    /**
     * ����������
     */
    public void onInit(){
        
        super.onInit();
        
        this.editblog.setAttribute("onclick", "linkymd.value='" + this.yyyymmdd +"';");
        this.editblog.setListener(this, "clickEditBlog");
        super.form.add(this.editblog);

        
        if(super.getContext().isForward()){
            this.showymd = this.yyyymmdd.substring(0,4) + "/" +
            			    this.yyyymmdd.substring(4,6) + "/" +
            			    this.yyyymmdd.substring(6) + " �̓��L";
        }
        
    }
    
    /**
     * �Z�L�����e�B�`�F�b�N
     */
    public boolean onSecurityCheck(){
    	return true;
    }
    
    
    /**
     * �f�d�s����
     */
    public void onGet(){
        super.setForward(MainPage.class);
    }
    
    /**
     * �o�n�r�s����
     */
    public void onPost(){
        
        if(!super.getContext().isForward()){
            return;
        }
        
        if(this.editblog.isClicked()){
            return;
        }

        String path = "/WEB-INF/data/blog/";
        path = path + this.yyyymmdd + ".txt";

        
        File f = new File(super.getContext().getServletContext().getRealPath(path));
        List list = ArUtil.readFile(super.getContext().getServletContext(), path);
        
        this.blg_title = (String)list.get(0);

        for(int i=1;i<list.size();i++){
        	this.blg_body = this.blg_body + (String)list.get(i) + "<br>" ;    
        }
        
    }
    
    
    /**
     * �߂�{�^������
     * @return
     */    
    public boolean clickBack(){
        
        Page next = super.getContext().createPage(MainPage.class);
        super.setForward(next);
        return false;
    }
    
    
    /**
     * �ҏW�{�^������
     * @return
     */
    public boolean clickEditBlog(){
        
        String ymd = super.linkymd.getValue();
        
        EditBlogPage next = (EditBlogPage)super.getContext().createPage(EditBlogPage.class);
        
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateformat = new SimpleDateFormat( ymd.substring(0,8) );
        String yyyymmdd = dateformat.format(date);
        
        next.setDate(yyyymmdd);
        super.setForward(next);
        
        return false;

    }
    
    
}

