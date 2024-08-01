use amp_logger::{log, LogLevel};

pub fn start() {

    load_configuration();

    amp_logger::log(LogLevel::Success, "Module amp_core successfully loaded".to_string());
}

fn load_configuration() {
    let path = std::env::args()
        .nth(1)
        .unwrap_or_else(|| "config.toml".to_string());

    log(LogLevel::Info, format!("Reading CFG at {}", path));

    amp_config::load_config(&path);
}