package jp.arcanum.clicklog.page;

import java.util.ArrayList;
import java.util.List;

import jp.arcanum.clicklog.util.ArUtil;
import net.sf.click.Page;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextArea;
import net.sf.click.control.TextField;

/**
 * 編集者プロファイルページ
 *
 */
public class EditProfilePage extends AbstractPage{
    
    
    private TextField p_name = new TextField("p_name");
    private TextField p_age  = new TextField("p_age");
    private TextField p_email = new TextField("p_email");
    private TextArea  p_body = new TextArea("p_body");
    
    private PasswordField oldpass = new PasswordField("oldpass");
    private PasswordField newpass1 = new PasswordField("newpass1");
    private PasswordField newpass2 = new PasswordField("newpass2");
    
    private Submit cancel = new Submit("cancel","キャンセル");
    private Submit ok     = new Submit("ok","O.K");
    
    
    /**
     * コンストラクタ
     *
     */
    public EditProfilePage(){
        
        this.p_name.setSize(20);
        super.form.add(this.p_name);
        this.p_age.setSize(3);
        super.form.add(this.p_age);
        this.p_email.setSize(30);
        super.form.add(this.p_email);
        this.p_body.setCols(32);
        this.p_body.setRows(20);
        super.form.add(this.p_body);        

        this.oldpass.setSize(20);
        super.form.add(this.oldpass);
        this.newpass1.setSize(20);
        super.form.add(this.newpass1);
        this.newpass2.setSize(20);
        super.form.add(this.newpass2);
        
        this.cancel.setListener(this, "clickCancel");
        super.form.add(this.cancel);
        
        this.ok.setListener(this, "clickOk");
        super.form.add(this.ok);
        
    }

    /**
     * ＧＥＴ処理
     */
    public void onGet(){
        super.setForward(MainPage.class);
    }
    
    /**
     * ＰＯＳＴ処理
     */
    public void onPost(){
        
        if(!super.getContext().isForward()){
            return;
        }
        
        
        String path = "/WEB-INF/data/profile/profile.txt";
        
        List list = ArUtil.readFile(super.getContext().getServletContext(), path);

        this.p_name.setValue((String)list.get(0));
        this.p_age.setValue((String)list.get(1));
        this.p_email.setValue((String)list.get(2));
        String bodyval = "";
        for(int i=3;i<list.size();i++){
            bodyval = bodyval + (String)list.get(i) + "\n" ;    
        }
        this.p_body.setValue(bodyval);

        
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
     * ＯＫ押下処理
     * @return
     */        
    public boolean clickOk(){
        
        List lines = new ArrayList();
        lines.add(this.p_name.getValue());
        lines.add(this.p_age.getValue());
        lines.add(this.p_email.getValue());
        lines.add(this.p_body.getValue());
        
        String path = "/WEB-INF/data/profile/profile.txt";

        ArUtil.writeFile(super.getContext().getServletContext(), path, lines);
        
        Page next = super.getContext().createPage(MainPage.class);
        super.setForward(next);
        return false;

    }

    
}

