Select MONTH(`privateclinicmanage`.`paymentdetail`.`created_date`) as 'Month' 
	 , SUM(`privateclinicmanage`.`paymentdetail`.`amount`) as "Revenue"
from `privateclinicmanage`.`paymentdetail`
where YEAR(`privateclinicmanage`.`paymentdetail`.`created_date`) = 2024
group by MONTH(`privateclinicmanage`.`paymentdetail`.`created_date`)
order by MONTH(`privateclinicmanage`.`paymentdetail`.`created_date`)