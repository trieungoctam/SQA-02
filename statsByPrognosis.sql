select `privateclinicmanage`.`medicine`.`name` as "Medicine's name" , SUM(`privateclinicmanage`.`prescription_items`.prognosis) as 'Total prognosis'
from `privateclinicmanage`.`medicine`
left join `privateclinicmanage`.`prescription_items` 
on `privateclinicmanage`.`medicine`.id = `privateclinicmanage`.`prescription_items`.medicine_id
inner join `privateclinicmanage`.`medical_examination` 
on  `privateclinicmanage`.`prescription_items`.medical_examination_id = `privateclinicmanage`.`medical_examination`.id
where year(`privateclinicmanage`.`medical_examination`.`created_date`) = 2024 
and month(`privateclinicmanage`.`medical_examination`.`created_date`) = 9
group by `privateclinicmanage`.`medicine`.`name`