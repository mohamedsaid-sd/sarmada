package com.sarmada.medical;

public class config {

    //public static final String main_url = "http://192.168.143.165/sarmada/";
    public static final String main_url =  "https://sarmadamedical.com/";
    // روابط الاضافة
    // اضافة مشكلة او مقترح
    public static final String add_feedback = main_url + "api/add/add_feedback.php";
    //اضافة طلب

    // الاضافات *******
    // اضافة الملف الطبي
    public static final String add_medical_file = main_url + "api/add/add_medical_file.php";

    // اضافة الطلب
    public static final String add_requset = main_url + "api/add/add_request.php";

    // العرض ********
    // عرض اصدارات التطبيق
    public static final String display_versions = main_url + "api/display/display_versions.php";
    // عرض الملف الطبي
    public static final String display_medical_file = main_url + "api/display/display_medical_file.php";

    public static final String display_notifications =  main_url + "api/display/display_notifications.php";
    public static final String display_profile = main_url + "api/display/display_customer_info.php";
    public static final String display_cat = main_url + "api/display/display_cat.php";
    public static final String display_main_cat = main_url + "api/display/display_maincat.php";
    //
    public static final String display_medical_cat = main_url + "api/display/display_medical_cat.php";

    public static final String display_doctor_sp = main_url + "api/display/display_doctor_sp.php";
    public static final String display_doctos = main_url + "api/display/display_doctors_where_specialization.php";
    // عرض الخدمات حسب المؤسسة
    public static final String display_services = main_url + "api/display/display_services_where_organization.php";

    // عرض مواد الطالب
    public static final String display_subject = main_url + "api/display/display_subjects.php";

    // عرض وحدات المواد

    public static final String display_unit = main_url + "api/display/display_units.php";

    // جلب معلومات الدرس
    public static final String display_lesson = main_url + "api/display/display_lessons.php";

    // edit url
    public static final String change_password = main_url + "api/edit/change_password.php";
    public static final String edit_report_customer = main_url + "api/edit/edit_report_customer.php";


    //login url's
    public static final String login_qr_code = main_url + "api/login/login_customer_qr.php";
    public static final String login_customer = main_url + "api/login/login_customer.php";
    //images url's

    //public static final String IMG_URL    = main_url + "admin/images/";
    public static final String QR_IMG_URL = main_url + "admin/";

    // الحذف ****************

    // حذف الملف الطبي
    public static final String delete_report_customer = main_url + "api/delete/delete_report_customer.php";

    // Link to display the classes
    public static final String classes = main_url + "api/display/classes.php";

}
