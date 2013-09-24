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
 * ブログ編集画面
 *
 */
public class EditBlogPage extends AbstractPage{
    
    //　編集中の日付    
    private HiddenField blg_date = new HiddenField("blg_date",String.class);
    public String yyyymmdd = "";
    
    //　内容
    private TextField blg_title = new TextField("blg_title");
    private TextArea  blg_body  = new TextArea("blg_body");
    
    //　メニュー
    private Submit cancel = new Submit("cancel","キャンセル");
    private Submit ok     = new Submit("ok","O.K");
    private Submit delete = new Submit("delete","削除");
    
    
    /**
     * コンストラクタ
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
     * 表示する日付設定
     * @param yyyymmdd この日付がファイル名称となる
     */
    public void setDate(String yyyymmdd){
        this.yyyymmdd = yyyymmdd;            
    }
    
    /**
     * 初期化処理
     */
    public void onInit(){
        
        super.onInit();
        
        if(super.getContext().isForward()){
            this.blg_date.setValueObject(this.yyyymmdd);
            this.yyyymmdd = yyyymmdd.substring(0,4) + "/" +
                            yyyymmdd.substring(4,6) + "/" +
                            yyyymmdd.substring(6) + " の日記";
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
     * GET処理
     */
    public void onGet(){
        super.setForward(MainPage.class);
    }
    
    /**
     * POST処理
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
     * キャンセル押下処理
     * @return
     */
    public boolean clickCancel(){
        
        Page next = super.getContext().createPage(MainPage.class);
        super.setForward(next);
        return false;
    }
    

    /**
     * ＯＫボタン処理
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
     * 削除ボタン処理
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

