Select *
from `privateclinicmanage`.medical_registry_list 
Inner join  `privateclinicmanage`.`schedule` on `privateclinicmanage`.medical_registry_list .schedule_id = `privateclinicmanage`.`schedule`.id
where YEAR(`privateclinicmanage`.`schedule`.`appointment_schedule`) = 2024 and month(`privateclinicmanage`.`schedule`.`appointment_schedule`) = 8 
	  and day(`privateclinicmanage`.`schedule`.`appointment_schedule`) = 14
	  and `privateclinicmanage`.`medical_registry_list`.`is_canceled` = false;

select `privateclinicmanage`.`user`.`email`
from `privateclinicmanage`.medical_registry_list 
left join `privateclinicmanage`.`user` on `privateclinicmanage`.medical_registry_list.`user_id` = `privateclinicmanage`.`user`.id
left join `privateclinicmanage`.`schedule` on `privateclinicmanage`.medical_registry_list.`schedule_id` = `privateclinicmanage`.`schedule`.id
left join `privateclinicmanage`.`status_is_approved` on `privateclinicmanage`.`medical_registry_list`.status_is_approved_id = `privateclinicmanage`.`status_is_approved`.id
where `privateclinicmanage`.`schedule`.id = 4 and `privateclinicmanage`.`status_is_approved`.id = 1
group by `privateclinicmanage`.`user`.`email`