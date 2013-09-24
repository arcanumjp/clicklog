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
     *�\������N����
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
     * �T�̃e�[�u��
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
     * �R���X�g���N�^
     * @param name ���̃R���g���[���̖��O
     */
	public ArCalendar(final String name){

		super(name);

        //�@�\������e�[�u���̐ݒ�
		Column colsun = new Column(SUN,"��");
		colsun.setEscapeHtml(false);
        Column colmon = new Column(MON,"��");
		colmon.setEscapeHtml(false);
        Column coltur = new Column(TUE,"��");
		coltur.setEscapeHtml(false);
        Column colwed = new Column(WED,"��");
		colwed.setEscapeHtml(false);
        Column colthi = new Column(THI,"��");
		colthi.setEscapeHtml(false);
        Column colfri = new Column(FRI,"��");
		colfri.setEscapeHtml(false);
        Column colsat = new Column(SAT,"�y");
		colsat.setEscapeHtml(false);

        //�@���`���̐ݒ�
        super.addColumn(colsun);
        super.addColumn(colmon);
        super.addColumn(coltur);
        super.addColumn(colwed);
        super.addColumn(colthi);
        super.addColumn(colfri);
        super.addColumn(colsat);
	}

    /**
     * �J�����_�[�ݒ�
     * @param calendar �\���������J�����_�[
     * @param page ����ArCalendar�C���X�^���X�̃I�[�i�[�y�[�W�C���X�^���X
     * @param method �C�x���g���\�b�h��
     */
	public final void setCalendar(
            final Calendar calendar,
            final Page page,
            final String method){

        //�@��ɋ�̃f�[�^�����Ă���
        List list = new ArrayList();
        for(int i = 0 ; i < 6 ; i++){
        	Map week = new HashMap();
        	week.put(MON, "�@");
        	week.put(TUE, "�@");
        	week.put(WED, "�@");
        	week.put(THI, "�@");
        	week.put(FRI, "�@");
        	week.put(SAT, "�@");
        	week.put(SUN, "�@");
            list.add(week);
        }

        //�@�J�����_�[�̐ݒ�@
        int orgmonth = calendar.get(Calendar.MONTH) + 1 ;
        int orgyear  = calendar.get(Calendar.YEAR);
        int row = 0;

        //�@�\������N�A���̎擾
        String y = Integer.toString(orgyear);
        String m = "00" + Integer.toString(orgmonth);
        m = m.substring(m.length()-2);

        //�@�\���J�����_�[�̎擾
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, orgyear);
        cal.set(Calendar.MONTH, orgmonth);

        //�@�J�����_�[�֓��t�̐ݒ�
        for(int i = 1 ; i < cal.getActualMaximum(Calendar.DATE)+1 ; i++){
        	cal.set(Calendar.DATE, i);
            int weeky = cal.get(Calendar.DAY_OF_WEEK);
            String key = (String)WEEK_TABLE.get(new Integer(weeky));

            Map week = (Map)list.get(row);

            String d = "00" + i;
            d = d.substring(d.length()-2);

            //�@���t�ɑΉ��������L�����݂���ꍇ�̓����N��\��
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
