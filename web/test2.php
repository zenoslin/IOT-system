		<?php
    // 指定返回格式为 JSON
    header("Content-type: text/json");

    // 获取当前时间，PHP 时间戳是秒为单位的，JS 中则是毫秒，所以这里乘以 1000
    $x = time() * 1000;
    // y 值为随机值
    $y = rand(0, 100);

    // 创建 PHP 数组，并最终用 json_encode 转换成 JSON 字符串
    $ret = array($x, $y);
    echo json_encode($ret);
?>
