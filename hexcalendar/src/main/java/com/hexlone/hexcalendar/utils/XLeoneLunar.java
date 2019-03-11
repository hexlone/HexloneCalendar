package com.hexlone.hexcalendar.utils;


import com.hexlone.hexcalendar.entity.DateInfo;

public class XLeoneLunar {

    private static boolean isLunarLeap = false;
    private static boolean isSolarLeap = false;
    private static DateInfo dateInfo = new DateInfo();
    private static Lunar preLunar;

    private static final int LUNAR_STATIC_DATA[] = {
            0x84B6BF, 0x04AE53, 0x0A5748, 0x5526BD, 0x0D2650, 0x0D9544, 0x46AAB9, 0x056A4D, 0x09AD42, 0x24AEB6,
            0x04AE4A, 0x6A4DBE, 0x0A4D52, 0x0D2546, 0x5D52BA, 0x0B544E, 0x0D6A43, 0x296D37, 0x095B4B, 0x749BC1,
            0x049754, 0x0A4B48, 0x5B25BC, 0x06A550, 0x06D445, 0x4ADAB8, 0x02B64D, 0x095742, 0x2497B7, 0x04974A,
            0x664B3E, 0x0D4A51, 0x0EA546, 0x56D4BA, 0x05AD4E, 0x02B644, 0x393738, 0x092E4B, 0x7C96BF, 0x0C9553,
            0x0D4A48, 0x6DA53B, 0x0B554F, 0x056A45, 0x4AADB9, 0x025D4D, 0x092D42, 0x2C95B6, 0x0A954A, 0x7B4ABD,
            0x06CA51, 0x0B5546, 0x555ABB, 0x04DA4E, 0x0A5B43, 0x352BB8, 0x052B4C, 0x8A953F, 0x0E9552, 0x06AA48,
            0x6AD53C, 0x0AB54F, 0x04B645, 0x4A5739, 0x0A574D, 0x052642, 0x3E9335, 0x0D9549, 0x75AABE, 0x056A51,
            0x096D46, 0x54AEBB, 0x04AD4F, 0x0A4D43, 0x4D26B7, 0x0D254B, 0x8D52BF, 0x0B5452, 0x0B6A47, 0x696D3C,
            0x095B50, 0x049B45, 0x4A4BB9, 0x0A4B4D, 0xAB25C2, 0x06A554, 0x06D449, 0x6ADA3D, 0x0AB651, 0x095746,
            0x5497BB, 0x04974F, 0x064B44, 0x36A537, 0x0EA54A, 0x86B2BF, 0x05AC53, 0x0AB647, 0x5936BC, 0x092E50,
            0x0C9645, 0x4D4AB8, 0x0D4A4C, 0x0DA541, 0x25AAB6, 0x056A49, 0x7AADBD, 0x025D52, 0x092D47, 0x5C95BA,
            0x0A954E, 0x0B4A43, 0x4B5537, 0x0AD54A, 0x955ABF, 0x04BA53, 0x0A5B48, 0x652BBC, 0x052B50, 0x0A9345,
            0x474AB9, 0x06AA4C, 0x0AD541, 0x24DAB6, 0x04B64A, 0x6a573D, 0x0A4E51, 0x0D2646, 0x5E933A, 0x0D534D,
            0x05AA43, 0x36B537, 0x096D4B, 0xB4AEBF, 0x04AD53, 0x0A4D48, 0x6D25BC, 0x0D254F, 0x0D5244, 0x5DAA38,
            0x0B5A4C, 0x056D41, 0x24ADB6, 0x049B4A, 0x7A4BBE, 0x0A4B51, 0x0AA546, 0x5B52BA, 0x06D24E, 0x0ADA42,
            0x355B37, 0x09374B, 0x8497C1, 0x049753, 0x064B48, 0x66A53C, 0x0EA54F, 0x06AA44, 0x4AB638, 0x0AAE4C,
            0x092E42, 0x3C9735, 0x0C9649, 0x7D4ABD, 0x0D4A51, 0x0DA545, 0x55AABA, 0x056A4E, 0x0A6D43, 0x452EB7,
            0x052D4B, 0x8A95BF, 0x0A9553, 0x0B4A47, 0x6B553B, 0x0AD54F, 0x055A45, 0x4A5D38, 0x0A5B4C, 0x052B42,
            0x3A93B6, 0x069349, 0x7729BD, 0x06AA51, 0x0AD546, 0x54DABA, 0x04B64E, 0x0A5743, 0x452738, 0x0D264A,
            0x8E933E, 0x0D5252, 0x0DAA47, 0x66B53B, 0x056D4F, 0x04AE45, 0x4A4EB9, 0x0A4D4C, 0x0D1541, 0x2D92B5
    };

    private static final double Term_D = 0.2422;

    private static final double Terms_C_21Century[] = {
            5.4055,20.12,
            3.87,  18.73,
            5.63,  20.646,
            4.81,  20.1,
            5.52,  21.04,
            5.678, 21.37,
            7.108, 22.83,
            7.5,   23.13,
            7.646, 23.042,
            8.318, 23.438,
            7.438, 22.36,
            7.18,  21.94,
    };

    private static final String Terms_Name[] = {
            "小寒","大寒",
            "立春","雨水",
            "惊蛰","春分",
            "清明","谷雨",
            "立夏","小满",
            "芒种","夏至",
            "小暑","大暑",
            "立秋","处暑",
            "白露","秋分",
            "寒露","霜降",
            "立冬","小雪",
            "大雪","冬至"
    };

    public static class Solar {
        int solarDay;
        int solarMonth;
        int solarYear;

        public Solar(int solarYear, int solarMonth, int solarDay) {
            this.solarYear = solarYear;
            this.solarMonth = solarMonth;
            this.solarDay = solarDay;
        }
    }
    public static class Lunar {
        int lunarDay;
        int lunarMonth;
        int lunarYear;

        public Lunar(int lunarYear, int lunarMonth, int lunarDay) {
            this.lunarYear = lunarYear;
            this.lunarMonth = lunarMonth;
            this.lunarDay = lunarDay;
        }
    }

    public static Lunar solarToLunar(int solarYear, int solarMonth, int solarDay){

        int index = solarYear-1900;
        int preYear = LUNAR_STATIC_DATA[index];
        int lunarYear = solarYear;
        //取最低7位 得 正月初一在公历里的日期
        int springFestivalInSolar = preYear & 0x7F;
        int springFestivalInSolar_Year = solarYear;
        //正月初一在公历里的月份
        int springFestivalInSolar_Month = springFestivalInSolar >> 5;
        //正月初一在公历里的天数
        int springFestivalInSolar_Day = springFestivalInSolar & 0x1F;

        if((solarMonth<springFestivalInSolar_Month) || (solarMonth == springFestivalInSolar_Month && solarDay<springFestivalInSolar_Day)){
            preYear = LUNAR_STATIC_DATA[index-1];
            lunarYear  = solarYear-1;
            //取最低7位 得 正月初一在公历里的日期
            springFestivalInSolar = preYear & 0x7F;
            springFestivalInSolar_Year = solarYear-1;
            //正月初一在公历里的月份
            springFestivalInSolar_Month = springFestivalInSolar >> 5;
            //正月初一在公历里的天数
            springFestivalInSolar_Day = springFestivalInSolar & 0x1F;
        }
        int lunarMonth = 0;
        int lunarDay = 0;
        int leapLunarMonth = recognizeIsLunarLeap(preYear);
        int betSo=betweenTwoSolarDate(springFestivalInSolar_Year,springFestivalInSolar_Month,springFestivalInSolar_Day,
                solarYear,solarMonth,solarDay);
        int approximateMonth = (betSo+1)/30;
        int preMon = preYear >> 7;
        preMon = preMon & 0x1FFF;
        int aa;
        int daysInLunar = 0;
        for(int i=1;i<approximateMonth+1;i++){
            aa = preMon >> (13-i);
            aa = aa & 0x1;
            if(recognizeIsLunarLeap(preYear)!=0) {
                daysInLunar = daysInLunar + (aa>0?30:29);
            }else {
                if(i == 13){
                    daysInLunar = daysInLunar;
                }else {
                    daysInLunar = daysInLunar + (aa>0?30:29);
                }
            }
        }
        if(daysInLunar<(betSo+1)){
            int be = betSo+1-daysInLunar;
            approximateMonth = approximateMonth+1;
            int numOneMonth = daysInOneLunarMonth(preYear,approximateMonth);
            if(be<=numOneMonth){
                lunarMonth = approximateMonth;
                lunarDay = be;
            }else {
                approximateMonth = approximateMonth+1;
                lunarMonth = approximateMonth;
                lunarDay = be-numOneMonth;
            }
        }else if(daysInLunar==(betSo+1)){
            lunarMonth = approximateMonth;
            lunarDay = daysInOneLunarMonth(preYear,approximateMonth);
        }
        if(isLunarLeap){
            if(approximateMonth<=leapLunarMonth){
                lunarMonth = approximateMonth;
            }else if((approximateMonth-leapLunarMonth)==1){
                lunarMonth = 0-leapLunarMonth;
            }else {
                lunarMonth = approximateMonth-1;
            }
        }
        //Log.e("noLeapLunar",lunarMonth+"   "+lunarDay);
        return new Lunar(lunarYear,lunarMonth,lunarDay);
    }


    private final static String[] LunarMonthName = {"","正", "二", "三", "四", "五",
            "六", "七", "八", "九", "十", "十一", "腊"};
    private final static String[] LunarDayName = {"","一", "二", "三", "四", "五",
            "六", "七", "八", "九", "十"};
    private final static String[] LunarDayName_First = {"初","十","廿","三十"};
    public static DateInfo getDateInfo(int solarYear, int solarMonth, int solarDay){

        dateInfo = new DateInfo();
        dateInfo.setSolarYear(solarYear);
        dateInfo.setSolarMonth(solarMonth);
        dateInfo.setSolarDay(solarDay);
        dateInfo.setSolarFestival(isSolarFestival(solarYear,solarMonth,solarDay));
        preLunar = solarToLunar(solarYear,solarMonth,solarDay);
        dateInfo.setLunarTerm(getTermStr(solarYear,solarMonth,solarDay));
        if(preLunar.lunarMonth>0){
            dateInfo.setLunarMonth(LunarMonthName[preLunar.lunarMonth]+"月");
        }else {
            dateInfo.setLunarMonth("闰"+LunarMonthName[-(preLunar.lunarMonth)]+"月");
        }
        if((preLunar.lunarDay/10)==0){
            dateInfo.setLunarDay("初"+LunarDayName[preLunar.lunarDay]);
        }else if(preLunar.lunarDay == 10){
            dateInfo.setLunarDay("初十");
        }else if(preLunar.lunarDay>10&&(preLunar.lunarDay/10)==1){
            dateInfo.setLunarDay("十"+LunarDayName[preLunar.lunarDay-10]);
        }else if(preLunar.lunarDay == 20){
            dateInfo.setLunarDay("二十");
        }else if(preLunar.lunarDay>20&&(preLunar.lunarDay/10)==2){
            dateInfo.setLunarDay("廿"+LunarDayName[preLunar.lunarDay-20]);
        }if(preLunar.lunarDay == 30){
            dateInfo.setLunarDay("三十");
        }
        dateInfo.setLunarFestival(isLunarFestival(preLunar.lunarYear,preLunar.lunarMonth,preLunar.lunarDay));
        dateInfo.setLunarYear(preLunar.lunarYear);
        return dateInfo;
    }


    private static int daysInOneLunarMonth(int year,int month){
        int preMon = year >> 7;
        preMon = preMon & 0x1FFF;
        int aa = preMon >> (13-month);
        aa = aa & 0x1;
        return aa>0?30:29;
    }

    /**
     * 判断农历年是否有闰月
     * @param preYear 农历年份
     * @return 0->not leap ; 具体数值则为闰月月份
     */
    private static  int recognizeIsLunarLeap(int preYear){
        int leapMonth = preYear >> 20;
        if(leapMonth!=0){
            isLunarLeap = true;
        }else {
            isLunarLeap = false;
        }
        return leapMonth;
    }

    /**
     * 计算一年里农历的总天数
     * @param preYear 农历年份
     * @return 农历的总天数
     */
    private static int getTotalNumberOfLunarDays(int preYear){
        int preMon = preYear >> 7;
        preMon = preMon & 0x1FFF;
        int aa;
        int totalDaysInLunar = 0;
        for(int i=1;i<14;i++){
            aa = preMon >> (13-i);
            aa = aa & 0x1;
            if(recognizeIsLunarLeap(preYear)!=0) {
                totalDaysInLunar = totalDaysInLunar + (aa>0?30:29);
            }else {
                if(i == 13){
                    totalDaysInLunar = totalDaysInLunar;
                }else {
                    totalDaysInLunar = totalDaysInLunar + (aa>0?30:29);
                }
            }
        }
        return totalDaysInLunar;

    }


    /**
     *判断公历年是否为闰年
     * @param year 公历年份
     * @return true->isLeap  ; false-> not Leap
     */
    public static boolean recognizeIsSolarLeap(int year){
        if(((year % 4==0) && (year % 100!=0)) || (year % 400==0)){
            isSolarLeap = true;
        }else {
            isSolarLeap = false;
        }
        return isSolarLeap;
    }

    /**
     * 两个公历日期间相差多少天
     * @param solarYear1 start year
     * @param solarMonth1 start month
     * @param solarDay1 start day
     * @param solarYear2 end year
     * @param solarMonth2 end month
     * @param solarDay2 end day
     * @return 天数
     */
    public static int betweenTwoSolarDate(int solarYear1,int solarMonth1,int solarDay1,
                                    int solarYear2,int solarMonth2,int solarDay2){


        int by = solarYear2-solarYear1;
        int num=0;
        if(by==0){
            num=betweenTwoSolarDateInOneYear(solarYear1,solarMonth1,solarDay1,solarYear2,solarMonth2,solarDay2);
        }else if(by==1 || by ==-1){
            if(by<0){
                int temp0 = solarYear1;
                int temp1 = solarMonth1;
                int temp2 = solarDay1;
                solarYear1 = solarYear2;
                solarYear2 = temp0;
                solarMonth1 = solarMonth2;
                solarMonth2 = temp1;
                solarDay1 = solarDay2;
                solarDay2 = temp2;
            }
            num=betweenTwoSolarDateInOneYear(solarYear1,solarMonth1,solarDay1,solarYear1,12,31)+
                    1+betweenTwoSolarDateInOneYear(solarYear2,1,1,solarYear2,solarMonth2,solarDay2);
            num=by>0?num:(0-num);
        }else if(by>1 || by<-1){
            if(by<0){
                int temp0 = solarYear1;
                int temp1 = solarMonth1;
                int temp2 = solarDay1;
                solarYear1 = solarYear2;
                solarYear2 = temp0;
                solarMonth1 = solarMonth2;
                solarMonth2 = temp1;
                solarDay1 = solarDay2;
                solarDay2 = temp2;
            }
            for(int i=solarYear1+1;i<solarYear2;i++){
                num=num+(recognizeIsSolarLeap(i)?366:365);
            }
            num=num+betweenTwoSolarDateInOneYear(solarYear1,solarMonth1,solarDay1,solarYear1,12,31)+
                    1+betweenTwoSolarDateInOneYear(solarYear2,1,1,solarYear2,solarMonth2,solarDay2);
            num=by>0?num:(0-num);
        }
        return num;
    }

    /**
     * 两个公历日期(在同一年)间相差多少天
     * @param solarYear1 start year
     * @param solarMonth1 start month
     * @param solarDay1 start day
     * @param solarYear2 end year == start year
     * @param solarMonth2 end month
     * @param solarDay2 end day
     * @return 天数
     */
    private static int betweenTwoSolarDateInOneYear(int solarYear1,int solarMonth1,int solarDay1,
                                              int solarYear2,int solarMonth2,int solarDay2){
        int[] bigSmallMonths;
        if(recognizeIsSolarLeap(solarYear1)){
            bigSmallMonths  = new int[]{0, 1, -1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1};
        }else {
            bigSmallMonths  = new int[]{0, 1, -2, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1};
        }

        int by = solarYear2-solarYear1;
        int bm = solarMonth2-solarMonth1;
        int num=0;
        if(by==0){
            if(bm>1 || bm<-1){
                int aa = (bm-1)*30;
                if(bm<0){
                    int temp1 = solarMonth1;
                    int temp2 = solarDay1;
                    solarMonth1 = solarMonth2;
                    solarMonth2 = temp1;
                    solarDay1 = solarDay2;
                    solarDay2 = temp2;
                    aa = (-1-bm)*30;
                }
                for(int i=solarMonth1+1;i<solarMonth2;i++){
                    aa = aa+bigSmallMonths[i];
                }
                aa=aa+(bigSmallMonths[solarMonth1]+30-solarDay1)+solarDay2;
                aa=bm>0?aa:(-1*aa);
                num=aa;
            }else if(bm==1||bm==-1){
                if(bm<0){
                    int temp2 = solarDay1;
                    solarMonth1 = solarMonth2;
                    solarDay1 = solarDay2;
                    solarDay2 = temp2;
                }
                int aa = 0;
                aa=aa+(bigSmallMonths[solarMonth1]+30-solarDay1)+solarDay2;
                aa=bm>0?aa:(-1*aa);
                num=aa;
            }else if(bm==0){
                int aa = 0;
                aa=aa+solarDay2-solarDay1;
                num=aa;
            }
        }else {
            num=404;
        }
        return num;
    }

    /**
     * 判断是否为24节气 返回节气名
     * [Y*D+C]-[L] 通式寿星公式
     * @param solarYear
     * @param solarMonth
     * @param solarDay
     * @return 返回节气名
     */
    public static String getTermStr(int solarYear,int solarMonth,int solarDay){
        String str = "";
        int index = solarMonth*2;
        int day1,day2;
        if(solarMonth<=1){
            day1 = ((int) ((solarYear-1-2000)*Term_D+Terms_C_21Century[index-1-1]))-((int)((solarYear-1-2000)/4));
            day2 = ((int) ((solarYear-1-2000)*Term_D+Terms_C_21Century[index-1]))-((int)((solarYear-1-2000)/4));
        }else {
            day1 = ((int) ((solarYear-2000)*Term_D+Terms_C_21Century[index-1-1]))-((int)((solarYear-2000)/4));
            day2 = ((int) ((solarYear-2000)*Term_D+Terms_C_21Century[index-1]))-((int)((solarYear-2000)/4));
        }
        if(solarDay==day1){
            str = Terms_Name[index-1-1];
        }else if(solarDay==day2){
            str = Terms_Name[index-1];
        }
        return str;
    }

    private static final int[] LunarFestivals = {
            0x101,0x10F,0x505,0x707,0x80F,0X909,0XC08
    };
    private static final String[] LunarFestivalsName = {
            "春节","元宵","端午","七夕","中秋","重阳","腊八"
    };

    public static String isLunarFestival(int lunarYear,int lunarMonth,int lunarDay){

        int dealed = (lunarMonth<<8)+lunarDay;
        //Log.e("startIs","startIs");
        switch (dealed){
            case 0x101:
                return "春节";
            case 0x10F:
                return "元宵";
            case 0x505:
                return "端午";
            case 0x707:
                return "七夕";
            case 0x80F:
                return "中秋";
            case 0X909:
                return "重阳";
            case 0XC08:
                return "腊八";
            default:
                break;
        }
        if(lunarMonth == 12){
            if(lunarDay == 23){
                return "小年";
            }
            int index = lunarYear-1900;
            int preYear = LUNAR_STATIC_DATA[index];
            int endLunarMonth;
            if(recognizeIsLunarLeap(lunarYear)==0){
                endLunarMonth = preYear >> 8;
            }else {
                endLunarMonth = preYear >> 7;
            }
            endLunarMonth = endLunarMonth & 0x1;
            int days;
            if(endLunarMonth==0){
                days = 29;
            }else {
                days = 30;
            }
            if(lunarDay == days){
                return "除夕";
            }
        }
        return "";
    }


    private static final int[] SolarFestivals = {
            0x101,0x20E,0x308,0x30C,0x30F,0x501,0x504,0x601,0x701,0x801,0x90A,0xA01,0xC18,0xC19
    };

    private static final String[] SolarFestivalName = {
            "元旦","情人节","妇女节","植树节","消权日","劳动节","青年节","儿童节","建党节","建军节","教师节","国庆节","平安夜","圣诞节"
    };

    public static String isSolarFestival(int solarYear,int solarMonth,int solarDay){
        int dealed = (solarMonth<<8)+solarDay;
        switch (dealed){
            case 0x20E:
                return "情人节";
            case 0x101:
                return "元旦";
            case 0x308:
                return "妇女节";
            case 0x30C:
                return "植树节";
            case 0x30F:
                return "消权日";
            case 0x501:
                return "劳动节";
            case 0X504:
                return "青年节";
            case 0X601:
                return "儿童节";
            case 0x701:
                return "建党节";
            case 0x801:
                return "建军节";
            case 0x90A:
                return "教师节";
            case 0xA01:
                return "国庆节";
            case 0xC18:
                return "平安夜";
            case 0xC19:
                return "圣诞节";
            default:
                break;
        }
        return "";
    }
}
