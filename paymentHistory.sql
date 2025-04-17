select pm.order_id , pm.created_date , mrl.`name` , pm.amount , pm.`description` , pm.result_code , pm.partner_code
from `privateclinicmanage`.`medical_registry_list` mrl
inner join `privateclinicmanage`.`paymentdetail` pm 
on mrl.payment_phase1_id = pm.id
where mrl.`name` = 'Nguyen Van A'
union
select pm.order_id ,  pm.created_date , mrl.`name` , pm.amount , pm.`description` , pm.result_code , pm.partner_code
from `privateclinicmanage`.`medical_registry_list` mrl
inner join `privateclinicmanage`.`medical_examination` me 
on me.medical_register_list_id = mrl.id
inner join `privateclinicmanage`.`paymentdetail` pm
on pm.id = me.payment_phase2_id
where mrl.`name` = 'Nguyen Van A';

