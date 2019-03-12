package call_recording.jbglass.in.callrecording.JSONBody;

public class AssignCallBody {
    String name,call_date,emp,title,subtitle,mob_no,hide;

    public AssignCallBody(String name,String call_date,String emp,String title,String subtitle,String mob_no,String hide){
        this.name=name;
        this.call_date=call_date;
        this.emp=emp;
        this.title=title;
        this.subtitle=subtitle;
        this.mob_no=mob_no;
        this.hide=hide;
    }
}
