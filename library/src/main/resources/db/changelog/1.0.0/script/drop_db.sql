DO $$
BEGIN
    drop table if exists link_hist cascade;
    drop table if exists file_hist cascade;
    drop table if exists file cascade;
    drop table if exists link cascade;
    drop table if exists link_status cascade;
    drop table if exists file_status cascade;
    drop table if exists file_group_member cascade;
    drop table if exists file_group cascade;
    drop table if exists error cascade;
    drop table if exists file_type cascade;
    drop table if exists file_group_attribute cascade;
    drop table if exists file_attribute cascade;
    drop table if exists file_group_hist cascade;
    drop table if exists file_group_rule cascade;
    drop table if exists file_group_condition cascade;
    drop table if exists fleg_flgr cascade;
    drop table if exists fleg_fleh cascade;
    drop table if exists process_type cascade;
    drop table if exists process_status cascade;
    drop table if exists process cascade;
    drop table if exists process_lock cascade;

    drop sequence if exists fle_seq;
    drop sequence if exists lnk_seq;
    drop sequence if exists lnkh_seq;
    drop sequence if exists fleg_seq;
    drop sequence if exists flgm_seq;
    drop sequence if exists fleh_seq;
    drop sequence if exists err_seq;
    drop sequence if exists flga_seq;
    drop sequence if exists flgh_seq;
    drop sequence if exists flgr_seq;
    drop sequence if exists prcd_seq;
    drop sequence if exists prlk_seq;

END $$;
