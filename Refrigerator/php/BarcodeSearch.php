<?php
$connect = new mysqli ( "mysql.hostinger.kr", "u291047931_admin", "q3tfMk26bU", "u291047931_db" ) or die ( "db���ӿ���" ); // DB�� �ִ� �ּ�(�̰��� �������� ���� �����ϴ� ���̱� ������ ������ �ּҸ� �ᵵ ��)
mysqli_select_db ( $connect, "u291047931_db" ) or die ( "DB ���� ����" ); // DB ����
mysqli_query ( $connect, "SET NAMES utf8" );
$type = $_REQUEST [type];
$code = $_REQUEST [code];
//$qry = "SELECT * FROM BarcodeInfo WHERE Code = $code";
$qry = "SELECT * FROM BarcodeInfo WHERE Type = '$type' AND Code = $code";
$result = mysqli_query ( $connect, $qry );

list( $returntype, $returncode, $name, $expirydate) = mysqli_fetch_array($result);
echo "{\"name\":$name,\"expridate\":$expirydate}";
?>
