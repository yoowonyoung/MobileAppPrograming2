<?php
$connect = mysqli_connect ( "mysql.hostinger.kr", "u291047931_admin", "fm8ldlNY4A", "u291047931_db" ) or die("db���ӿ���"); // DB�� �ִ� �ּ�(�̰��� �������� ���� �����ϴ� ���̱� ������ ������ �ּҸ� �ᵵ ��)
mysqli_select_db ($connect,"u291047931_db" ) or die("DB ���� ����"); // DB ����
mysqli_query ( $connect ,"set names utf8" ); // �̰� ���� �ѱ�(utf8)�� �����ϱ� ���� ��

$title = $_REQUEST [title]; // �ּҿ� �ִ� name���� �޾� name ������ ����



$qry = "insert into Test(title) values('$title');";
$result = mysqli_query ( $connect , $qry );

$xmlcode = "<?xml version = \"1.0\" encoding = \"utf-8\"?-->\n"; // xml���Ͽ� ����� �ڵ�
$xmlcode .= "<result>$result</result>\n"; // DB insert�� ���������� �ƴ��� ���θ� Ȯ���ϱ� ���� result���� xml�� ��½�Ŵ

$dir = "/home/u291047931/public_html"; // insertresult.xml ������ ������ ���
$filename = $dir . "/insertresult.xml";

file_put_contents ( $filename, $xmlcode ); // xmlcode�� ������ xml���Ϸ� ���
?>