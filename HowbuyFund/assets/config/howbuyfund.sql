Begin Transaction;
Create TABLE IF NOT EXISTS FundsInfo(
[_id] integer PRIMARY KEY AUTOINCREMENT
[code] text
,[name] text
,[pinyin] text
,[type] text
,[jjfl2] text
,[xuan] integer DEFAULT -1
,[unit_netvalue] text
,[state] text
,[status] integer
,[tradeflag] integer DEFAULT 0
,[hbflag] integer DEFAULT 0
,[date] LONG DEFAULT 0
);

Create  TABLE IF NOT EXISTS FundsInfoOpt(
[code] text PRIMARY KEY
,[xuantime] text
,[postion] integer
   
);

Create TABLE IF NOT EXISTS NetValue(
[jjdm] text PRIMARY KEY DEFAULT NULL
,[jzrq] text DEFAULT NULL
,[jjjz] text DEFAULT NULL
,[ljjz] text DEFAULT NULL
,[jjjg] text DEFAULT NULL
,[sgbz] integer DEFAULT NULL
,[shbz] integer DEFAULT NULL
,[dtbz] integer DEFAULT NULL
,[zfxz] text DEFAULT NULL
,[cxzz] text DEFAULT NULL
,[zyjl] text DEFAULT NULL
,[hbdr] text DEFAULT NULL
,[hb1Y] text DEFAULT NULL
,[hb3Y] text DEFAULT NULL
,[hb6Y] text DEFAULT NULL
,[hb1N] text DEFAULT NULL
,[hbjn] text DEFAULT NULL
,[dthb1N] text DEFAULT NULL
,[dthb2N] text DEFAULT NULL
,[dthb3N] text DEFAULT NULL
,[dthb4N] text DEFAULT NULL
,[dthb5N] text DEFAULT NULL
,[qrsy] text DEFAULT NULL
,[wfsy] text DEFAULT NULL
);
CREATE TABLE [tb_char_value] (
  [id] TEXT DEFAULT NULL, 
  [value] REAL DEFAULT NULL, 
  [date] TEXT DEFAULT NULL, 
  [type] INTEGER DEFAULT 0);
Commit Transaction;