/**
 * copy right reserved arcanum.jp 2007
 * 
 */
package jp.arcanum.clicklog.page;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import jp.arcanum.clicklog.page.control.ArCalendar;
import jp.arcanum.clicklog.page.control.Contents;
import jp.arcanum.clicklog.util.ArUtil;
import net.sf.click.Page;
import net.sf.click.control.ActionLink;
import net.sf.click.control.Column;
import net.sf.click.control.Form;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Label;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Select;
import net.sf.click.control.Submit;
import net.sf.click.control.Table;
import net.sf.click.util.HtmlStringBuffer;

/**
 * メイン画面
 *
 */
public class AbstractPage extends Page{
    
 
    // ---- スタイル関係
    public String tablestype = "its"; 
    /* isi、its、mars、nocol、report、simple　*/ 
    private Select selstyle = new Select("selstyle");
    
    // ---- 
    protected HiddenField linkymd = new HiddenField("linkymd",String.class);
    private Submit showblog = new Submit("showblog");
    
    // ---- ヘッダ関係
    public String hd_title = "";
    public String hd_desc  = "";
    
    // ---- ログイン関係
    private PasswordField log_password  = new PasswordField("log_password");
    private Submit        log_ok  = new Submit("log_ok","ログイン");
    private Submit        log_off = new Submit("log_off","ログオフ");

    // ---- カレンダー関係
    public String cal_month = ""; // とりあえず
    public ActionLink cal_prevmonth = new ActionLink("cal_prevmonth" , "<< Prev ");
    public ActionLink cal_nextmonth = new ActionLink("cal_nextmonth" , " Next >>");
    public ArCalendar cal_calendar = new ArCalendar("cal_calendar");
    
    // ---- プロファイル関係
    public String        pro_name  = "";
    public Integer       pro_age   = null;
    public Label         pro_email = new Label("pro_email");    
    public String        pro_body  = "";
    private Submit  pro_edit  = new Submit("pro_edit","編集"){
        public String toString(){
            HttpSession sess = super.getContext().getSession();
            if(sess.getAttribute("LOGIN")==null){
                return "";
            }
            return super.toString();
        }        
    };
    
    // ---- 最近の一覧
    public Table lst_lately = new Table("lst_lately"); 
    
    
    // ---- その他
    public Form form = new Form();
        
    
    /**
     * コンストラクタ
     *
     */    
    public AbstractPage(){
        
        // ---- スタイル関係初期化
        selstyle.add("isi");
        selstyle.add("its");
        selstyle.add("mars");
        selstyle.add("nocol");
        selstyle.add("report");
        selstyle.add("simple");
        this.selstyle.setListener(this, "clickStyle");
        this.selstyle.setAttribute("onchange", "form.submit()");
        this.form.add(this.selstyle);
        
        
        // ---- カレンダー関係初期化
        this.cal_calendar.setAttribute("class", this.tablestype);
        super.addControl(this.cal_calendar);
        this.cal_nextmonth.setListener(this, "clickNext");
        this.cal_prevmonth.setListener(this, "clickPrev");
        super.addControl(cal_nextmonth);
        super.addControl(cal_prevmonth);
        
        // ---- プロフィール関係初期化
        super.addControl(this.pro_email);
        this.pro_edit.setListener(this, "clickEdit");
        this.form.add(this.pro_edit);
        
        // ---- ログイン関係初期化
        this.form.add(this.log_password);
        this.log_ok.setListener(this, "clickLogin");
        this.form.add(this.log_ok);
        this.log_off.setListener(this, "clickLogoff");
        this.form.add(this.log_off);
        
        // ---- 最近の一覧
        Column lstcol = new Column("CONTENTS","最近の投稿");
        lstcol.setEscapeHtml(false);
        this.lst_lately.addColumn(lstcol);
        this.lst_lately.setAttribute("class", this.tablestype);
        this.lst_lately.setWidth("100%");
        
        // ---- ブログ表示関係の設定
        this.showblog.setListener(this , "clickBlogLink");
        this.showblog.setAttribute("style", "display:none");
        this.form.add(this.showblog);
        this.form.add(this.linkymd);
        
        
    }

    /**
     * テンプレートファイル取得
     */
    public String getTemplate(){
        return "pages/template.htm";
    }
    

    /**
     * 初期化処理
     */
    public void onInit(){
        
        String path = "/WEB-INF/data/conf/blogconf";
        this.hd_title = ArUtil.getProperty(super.getContext().getServletContext(), path, "header.title");
        this.hd_desc = ArUtil.getProperty(super.getContext().getServletContext(), path, "header.desc");
        
        HttpSession sess = super.getContext().getSession();
        String style = (String)sess.getAttribute("STYLE");
        if(style == null){
            style = "isi";
        }
        this.selstyle.setValue(style);
        this.tablestype = style;
        
    }
    
    /**
     * セキュリティチェック処理
     */
    public boolean onSecurityCheck(){

        HttpSession sess = super.getContext().getSession();
        if(sess.getAttribute("LOGIN")==null){
            super.setForward(MainPage.class);
            return false;
        }
        
        return true;
    }

    
    /**
     * 描画前処理
     */
    public void onRender(){
        
        //　プロフィール表示
        this.setProfile();
        
        //　最近の一覧表示
        this.setLaytelyList();
        
        //　カレンダー表示
        this.setCalendar();
        
    }
    
    /**
     * カレンダー設定
     *
     */
    private void setCalendar(){
    	
        HttpSession sess = super.getContext().getSession();
        Calendar cal = (Calendar)sess.getAttribute("CALENDAR");
        if(cal == null){
            cal = Calendar.getInstance();
        }
        this.cal_calendar.setCalendar(cal,this,"clickBlogLink");
        
        String y = Integer.toString(cal.get(Calendar.YEAR));
        String m = Integer.toString(cal.get(Calendar.MONTH) + 1 );
        this.cal_month =  y + "/" + m;

    }

    
    
    /**
     * プロファイル設定
     *
     */
    private void setProfile(){
        
        String path = "/WEB-INF/data/profile/profile.txt";
        
        List list = ArUtil.readFile(super.getContext().getServletContext(), path);
        
        this.pro_name = (String)list.get(0);
        this.pro_age  = new Integer((String)list.get(1));
        
        String emailstr = (String)list.get(2);
        HtmlStringBuffer emailbuf = new HtmlStringBuffer();
        emailbuf.elementStart("a");
        emailbuf.appendAttribute("href", "mailto:" + emailstr);
        emailbuf.elementEnd();
        emailbuf.append(emailstr);
        emailbuf.elementEnd("a");
        this.pro_email.setLabel(emailbuf.toString());
        
        for(int i=3;i<list.size();i++){
            this.pro_body = this.pro_body + (String)list.get(i) ;    
        }
        
        
    }
    
    /**
     * 最近の一覧設定
     *
     */
    private void setLaytelyList(){
        
        //　ファイルを読み込む
        String path = "/WEB-INF/data/blog/";
        String realpath = super.getContext().getServletContext().getRealPath(path);
        File dir = new File(realpath);
        File[] filelist = dir.listFiles();
        //　読み込んだ内容を新しい順に並べる
        TreeMap filetree = new TreeMap(new Comparator(){
            public int compare( Object object1, Object object2 ){
                return ( (Comparable)object1 ).compareTo( object2 ) * -1;
            }
        });
        
        for(int i = 0 ; i < filelist.length ; i++){
            filetree.put(filelist[i].getName(), filelist[i]);
        }

        List rowlist = new ArrayList();
        
        Iterator ite = filetree.keySet().iterator();
        int maxrow = 10;
        int prerow = 0;
        while(ite.hasNext()){

            Map row = new HashMap();
            
            File f = (File)filetree.get(ite.next());
            String yyyymmdd = f.getName().substring(0,f.getName().lastIndexOf("."));
            yyyymmdd = yyyymmdd.substring(0,8);
            
            Contents con = new Contents(yyyymmdd,true);
            
            con.setAttribute("onclick", "linkymd.value='" + con.getName()+"';showblog.click();return false;");
            
            super.addControl(con);
            row.put("CONTENTS", con);
            rowlist.add(row);
            
            prerow++;
            if(prerow>maxrow){
                break;
            }
        }
        
        this.lst_lately.setRowList(rowlist);                
        
        
    }
    

    /**
     * << クリック
     * @return
     */
    public boolean clickPrev(){
        this.moveMonth(-1);
        return true;
    }

    /**
     * >>クリック
     * @return
     */
    public boolean clickNext(){
        this.moveMonth(1);
        return true;
    }
    
    /**
     * 月の移動
     * @param amount 移動したい分の数。
     */
    private void moveMonth(final int amount){
        
        HttpSession sess = super.getContext().getSession();
        
        Calendar cal = (Calendar)sess.getAttribute("CALENDAR");
        if(cal == null){
            cal = Calendar.getInstance();
        }
        cal.add(Calendar.MONTH, amount);
        sess.setAttribute("CALENDAR", cal);
        
    }

    
    /**
     * ログインボタン処理
     * @return
     */
    public boolean clickLogin(){
        
        String path = "/WEB-INF/data/conf/password";
        
        String pass = this.log_password.getValue();
        String pass_ = ArUtil.getProperty(super.getContext().getServletContext(), path, "password");
        if(pass_.equals(pass)){
            
            HttpSession sess = super.getContext().getSession();
            sess.setAttribute("LOGIN","LOGIN");
            return true;
            
        }
        return true;
    }
    
    
    /**
     * ログオフボタン処理
     * @return
     */
    public boolean clickLogoff(){
        
        HttpSession sess = super.getContext().getSession();
        sess.removeAttribute("LOGIN");
        
        Page next = super.getContext().createPage(MainPage.class);
        super.setForward(next);
        return false;

    }
    
    
    /**
     * 編集ボタン処理
     * @return
     */
    public boolean clickEdit(){
        
        Page next = super.getContext().createPage(EditProfilePage.class);
        super.setForward(next);
        
        return false;
    
    }

    /**
     * ブログリンク処理
     * @return
     */
    public boolean clickBlogLink(){
        
        String ymd = this.linkymd.getValue();
        ymd = ymd.substring(0,8);
        
        ShowBlogPage next = (ShowBlogPage)super.getContext().createPage(ShowBlogPage.class);
        
        next.setDate(ymd);
        super.setForward(next);
        
        return false;
          
    }
    
    /**
     * スタイル選択処理
     * @return
     */
    public boolean clickStyle(){
        
        this.tablestype = this.selstyle.getValue();
        HttpSession sess = super.getContext().getSession();
        sess.setAttribute("STYLE", this.tablestype);
        
        return true;
    }


}

