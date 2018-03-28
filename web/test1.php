<?php
  // 指定返回格式为 JSON
  header("Content-type: text/json");

  require("db_config.php");
  $conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
  mysql_query("set names 'utf8'"); //数据库输出编码
  mysql_select_db($mysql_database); //打开数据库

  $result = mysql_query("select * from my_table order by Time desc limit 1", $conn);
  $data = mysql_fetch_assoc($result);
  // 获取当前时间，PHP 时间戳是秒为单位的，JS 中则是毫秒，所以这里乘以 1000
  // 创建 PHP 数组，并最终用 json_encode 转换成 JSON 字符串
  $ret = array(
    'time' => time() * 1000,
    'id' => $data['id'],
    'Temp' => $data['Temp'],
    'Humity' => $data['Humity'],
    'Light' => $data['Light'],
    'Smoke' => $data['Smoke']
  );
  echo json_encode($ret);
?>
