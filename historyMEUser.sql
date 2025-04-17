SELECT mrl.name,
       MAX(me.created_date) AS 'Ngày khám gần nhất',
       COUNT(me.id) AS 'Tổng số lần khám'
FROM `privateclinicmanage`.`medical_registry_list` mrl
inner JOIN `privateclinicmanage`.`medical_examination` me
ON mrl.id = me.medical_register_list_id
where mrl.user_id = 1
GROUP BY mrl.name;