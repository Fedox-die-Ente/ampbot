use std::time::{SystemTime, UNIX_EPOCH};
use chrono::{DateTime, NaiveDateTime, Utc};
use colored::{ColoredString, Colorize};

pub enum LogLevel {
    Info,
    Warn,
    Error,
    Success,
    Debug,
}

impl LogLevel {
    pub(crate) fn value(&self) -> ColoredString {
        match self {
            LogLevel::Info => "INFO".blue().bold(),
            LogLevel::Warn => "WARN".yellow().bold(),
            LogLevel::Error => "ERROR".red().bold(),
            LogLevel::Success => "SUCCESS".green().bold(),
            LogLevel::Debug => "DEBUG".purple().bold(),
        }
    }
}

pub fn log(log_level: LogLevel, string: String) {
    println!("{} [{}] => {}", get_current_time(), log_level.value(), string);
}

pub fn get_current_time() -> String {
    let naive = NaiveDateTime::from_timestamp(SystemTime::now().duration_since(UNIX_EPOCH).unwrap().as_secs() as i64, 0);

    let datetime: DateTime<Utc> = DateTime::from_utc(naive, Utc);
    let new_date = datetime.format("%Y-%m-%d %H:%M:%S");

    return new_date.to_string();
}