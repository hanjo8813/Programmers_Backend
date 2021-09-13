import React from "react";

export function SummaryItem(productName, counts) {
    return (
        <div className="row">
            <h6 className="p-0">
                {productName}
                <span className="badge bg-dark text-">{counts}개</span>
            </h6>
        </div>
    )

}