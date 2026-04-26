CREATE OR REPLACE FUNCTION getLockProcess(p_pctp_id int4) RETURNS INT4 AS
$$
DECLARE
	lv_prcd_id int4;
	lv_prlk_id int4;
BEGIN
 	select p.prcd_id
	from process p
	into lv_prcd_id
	where p.pctp_pctp_id = p_pctp_id
		  and ( p.pcst_pcst_id = 1 or (p.pcst_pcst_id = 4 and p.attempts_remaining > 0))
	order by random()
	limit 1
	for update nowait;

	if (lv_prcd_id is null) then
       raise no_data_found;
    end if;

    lv_prlk_id := nextval('prlk_seq');
	insert into process_lock(prlk_id, prcd_prcd_id) values(lv_prlk_id, lv_prcd_id);
	update process set pcst_pcst_id = 2, start_date = now(), end_date = null, err_err_id = null, attempts_remaining = attempts_remaining - 1
	where prcd_id = lv_prcd_id;

    return lv_prcd_id;
EXCEPTION

WHEN unique_violation THEN
	CALL getLockProcess(p_pctp_id);
WHEN no_data_found THEN
	return -1;
WHEN lock_not_available then
	return -1;

END;
$$ LANGUAGE plpgsql;