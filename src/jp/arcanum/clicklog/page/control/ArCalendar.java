package jp.arcanum.clicklog.page.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.click.Page;
import net.sf.click.control.ActionLink;
import net.sf.click.control.Column;
import net.sf.click.control.Table;

public class ArCalendar extends Table {

	/**
     *表示する年月日
	 */
	private Calendar _calendar = null;

	public static final String SUN = "SUN";
	public static final String MON = "MON";
	public static final String TUE = "TUE";
	public static final String WED = "WED";
	public static final String THI = "THI";
	public static final String FRI = "FRI";
	public static final String SAT = "SAT";

    /**
     * 週のテーブル
     */
	public static final Map WEEK_TABLE = new HashMap();
	static {
		WEEK_TABLE.put(new Integer(Calendar.MONDAY   ), MON);
		WEEK_TABLE.put(new Integer(Calendar.TUESDAY  ), TUE);
		WEEK_TABLE.put(new Integer(Calendar.WEDNESDAY), WED);
		WEEK_TABLE.put(new Integer(Calendar.THURSDAY ), THI);
		WEEK_TABLE.put(new Integer(Calendar.FRIDAY   ), FRI);
		WEEK_TABLE.put(new Integer(Calendar.SATURDAY ), SAT);
		WEEK_TABLE.put(new Integer(Calendar.SUNDAY   ), SUN);
	}

    /**
     * コンストラクタ
     * @param name このコントロールの名前
     */
	public ArCalendar(final String name){

		super(name);

        //　表示するテーブルの設定
		Column colsun = new Column(SUN,"日");
		colsun.setEscapeHtml(false);
        Column colmon = new Column(MON,"月");
		colmon.setEscapeHtml(false);
        Column coltur = new Column(TUE,"火");
		coltur.setEscapeHtml(false);
        Column colwed = new Column(WED,"水");
		colwed.setEscapeHtml(false);
        Column colthi = new Column(THI,"木");
		colthi.setEscapeHtml(false);
        Column colfri = new Column(FRI,"金");
		colfri.setEscapeHtml(false);
        Column colsat = new Column(SAT,"土");
		colsat.setEscapeHtml(false);

        //　月～日の設定
        super.addColumn(colsun);
        super.addColumn(colmon);
        super.addColumn(coltur);
        super.addColumn(colwed);
        super.addColumn(colthi);
        super.addColumn(colfri);
        super.addColumn(colsat);
	}

    /**
     * カレンダー設定
     * @param calendar 表示したいカレンダー
     * @param page このArCalendarインスタンスのオーナーページインスタンス
     * @param method イベントメソッド名
     */
	public final void setCalendar(
            final Calendar calendar,
            final Page page,
            final String method){

        //　先に空のデータを入れておく
        List list = new ArrayList();
        for(int i = 0 ; i < 6 ; i++){
        	Map week = new HashMap();
        	week.put(MON, "　");
        	week.put(TUE, "　");
        	week.put(WED, "　");
        	week.put(THI, "　");
        	week.put(FRI, "　");
        	week.put(SAT, "　");
        	week.put(SUN, "　");
            list.add(week);
        }

        //　カレンダーの設定　
        int orgmonth = calendar.get(Calendar.MONTH) + 1 ;
        int orgyear  = calendar.get(Calendar.YEAR);
        int row = 0;

        //　表示する年、月の取得
        String y = Integer.toString(orgyear);
        String m = "00" + Integer.toString(orgmonth);
        m = m.substring(m.length()-2);

        //　表示カレンダーの取得
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, orgyear);
        cal.set(Calendar.MONTH, orgmonth);

        //　カレンダーへ日付の設定
        for(int i = 1 ; i < cal.getActualMaximum(Calendar.DATE)+1 ; i++){
        	cal.set(Calendar.DATE, i);
            int weeky = cal.get(Calendar.DAY_OF_WEEK);
            String key = (String)WEEK_TABLE.get(new Integer(weeky));

            Map week = (Map)list.get(row);

            String d = "00" + i;
            d = d.substring(d.length()-2);

            //　日付に対応した日記が存在する場合はリンクを表示
            String yyyymmdd = y + m + d;
            String path="/WEB-INF/data/blog/" + yyyymmdd + ".txt";
            File f = new File(super.getContext().getServletContext().getRealPath(path));
            if(f.exists()){
                ActionLink linky = new ActionLink(
                        yyyymmdd + "_CAL",
                        "<b>" + Integer.toString(i) + "</b>");
                page.addControl(linky);
                week.put(key, linky);
                linky.setAttribute("onclick", "linkymd.value='" + linky.getName()+"';showblog.click();return false;");
            }
            else{
                week.put(key, new Integer(i));
            }

            if(key.equals("SAT")){
                row++;
            }

        }
        super.setRowList(list);


	}

}
