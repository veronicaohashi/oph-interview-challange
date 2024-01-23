CREATE TABLE IF NOT EXISTS incomes(
    id UUID NOT NULL,
    description VARCHAR(250) NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    date date NOT NULL,
    financial_statement_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_incomes PRIMARY KEY (id),
    CONSTRAINT fk_income_financial_statement_id FOREIGN KEY (financial_statement_id)
            REFERENCES financial_statements (id)
)
