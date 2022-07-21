output "access_token_cli_command" {
  value = "az account get-access-token --resource ${var.application_uri}"
}