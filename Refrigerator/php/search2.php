<?php
$connect = new mysqli ( "mysql.hostinger.kr", "u291047931_admin", "fm8ldlNY4A", "u291047931_db" ) or die ( "db���ӿ���" ); // DB�� �ִ� �ּ�(�̰��� �������� ���� �����ϴ� ���̱� ������ ������ �ּҸ� �ᵵ ��)
mysqli_select_db ( $connect, "u291047931_db" ) or die ( "DB ���� ����" ); // DB ����
mysqli_query ( $connect, "SET NAMES utf8" );

$qry = "SELECT * FROM Test";
$result = mysqli_query ( $connect, $qry );
$total_record = $result->num_rows;

echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";
for($i = 0; $i < $total_record; $i ++) {
	if ($result->data_seek ( $i )) {
		$row = $result->fetch_array ();
		echo "{\"title\":\"$row[title]\"},";
	} else {
		echo "No Record";
	}
}
echo "]}";
?>
