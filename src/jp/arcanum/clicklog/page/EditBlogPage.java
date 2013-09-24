package jp.arcanum.clicklog.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.arcanum.clicklog.util.ArUtil;
import net.sf.click.Page;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextArea;
import net.sf.click.control.TextField;

/**
 * �u���O�ҏW���
 *
 */
public class EditBlogPage extends AbstractPage{
    
    //�@�ҏW���̓��t    
    private HiddenField blg_date = new HiddenField("blg_date",String.class);
    public String yyyymmdd = "";
    
    //�@���e
    private TextField blg_title = new TextField("blg_title");
    private TextArea  blg_body  = new TextArea("blg_body");
    
    //�@���j���[
    private Submit cancel = new Submit("cancel","�L�����Z��");
    private Submit ok     = new Submit("ok","O.K");
    private Submit delete = new Submit("delete","�폜");
    
    
    /**
     * �R���X�g���N�^
     *
     */
    public EditBlogPage(){
        
        super.form.add(this.blg_date);
        
        this.blg_title.setSize(50);
        super.form.add(this.blg_title);
        this.blg_body.setRows(30);
        this.blg_body.setCols(50);
        super.form.add(this.blg_body);
        
        this.cancel.setListener(this, "clickCancel");
        super.form.add(this.cancel);
        
        this.ok.setListener(this, "clickOk");
        super.form.add(this.ok);
        
        this.delete.setListener(this, "clickDelete");
        super.form.add(this.delete);
        
    }

    
    /**
     * �\��������t�ݒ�
     * @param yyyymmdd ���̓��t���t�@�C�����̂ƂȂ�
     */
    public void setDate(String yyyymmdd){
        this.yyyymmdd = yyyymmdd;            
    }
    
    /**
     * ����������
     */
    public void onInit(){
        
        super.onInit();
        
        if(super.getContext().isForward()){
            this.blg_date.setValueObject(this.yyyymmdd);
            this.yyyymmdd = yyyymmdd.substring(0,4) + "/" +
                            yyyymmdd.substring(4,6) + "/" +
                            yyyymmdd.substring(6) + " �̓��L";
        }
        
        String path = "/WEB-INF/data/blog/";
        String date = (String)this.blg_date.getValueObject();
        path = path + date + ".txt";
        path = super.getContext().getServletContext().getRealPath(path);
        if(!new File(path).exists()){
            this.delete.setDisabled(true);
        }

        
    }
    
    /**
     * GET����
     */
    public void onGet(){
        super.setForward(MainPage.class);
    }
    
    /**
     * POST����
     */
    public void onPost(){
        
        if(!super.getContext().isForward()){
            return;
        }
        

        String path = "/WEB-INF/data/blog/";
        String date = (String)this.blg_date.getValueObject();
        path = path + date + ".txt";

        
        File f = new File(super.getContext().getServletContext().getRealPath(path));
        if(f.exists()){
            List list = ArUtil.readFile(super.getContext().getServletContext(), path);
            
            this.blg_title.setValue((String)list.get(0));

            String bodyval = "";
            for(int i=1;i<list.size();i++){
                bodyval = bodyval + (String)list.get(i) + "\n" ;    
            }
            this.blg_body.setValue(bodyval);
        
        }
        
    }
    
    
    /**
     * �L�����Z����������
     * @return
     */
    public boolean clickCancel(){
        
        Page next = super.getContext().createPage(MainPage.class);
        super.setForward(next);
        return false;
    }
    

    /**
     * �n�j�{�^������
     * @return
     */
    public boolean clickOk(){
        
        
        String path = "/WEB-INF/data/blog/";
        String date = (String)this.blg_date.getValueObject();
        path = path + date + ".txt";
        
        List lines = new ArrayList();
        lines.add(this.blg_title.getValue());
        lines.add(this.blg_body.getValue());

        ArUtil.writeFile(super.getContext().getServletContext(), path, lines);
        
        Page next = super.getContext().createPage(MainPage.class);
        super.setForward(next);
        return false;

    }

    
    /**
     * �폜�{�^������
     * @return
     */
    public boolean clickDelete(){
        
        String path = "/WEB-INF/data/blog/";
        String date = (String)this.blg_date.getValueObject();
        path = path + date + ".txt";
        path = super.getContext().getServletContext().getRealPath(path);
        
        new File(path).delete();    
        
        Page next = super.getContext().createPage(MainPage.class);
        super.setForward(next);
        return false;

    }

    
}

