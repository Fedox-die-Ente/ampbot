pub fn start() {

    load_configuration();

    println!("Core is running...")

}

fn load_configuration() {
    let path = std::env::args()
        .nth(1)
        .unwrap_or_else(|| "config.toml".to_string());

    println!("[AMP] Reading CFG at {}", path);

    amp_config::load_config(&path);
}