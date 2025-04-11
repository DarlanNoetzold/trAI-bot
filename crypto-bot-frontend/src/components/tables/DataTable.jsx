import React from 'react';
import PropTypes from 'prop-types';

const DataTable = ({ data = [], columns = [], onAction, actionLabel }) => {
  if (!Array.isArray(data) || !Array.isArray(columns)) {
    return <div className="text-red-500">Erro ao exibir tabela: dados ou colunas inválidos.</div>;
  }

  return (
    <div className="overflow-x-auto rounded-lg shadow border border-green-900 bg-[#101b10]">
      <table className="min-w-full text-sm text-left text-green-100 font-mono">
        <thead className="bg-[#1c2b1c] text-green-300 border-b border-green-900">
          <tr>
            {columns.map((col) => (
              <th key={col} className="px-4 py-2 whitespace-nowrap">
                {col.toUpperCase()}
              </th>
            ))}
            {onAction && <th className="px-4 py-2">Ações</th>}
          </tr>
        </thead>
        <tbody>
          {data.map((row, rowIndex) => (
            <tr
              key={rowIndex}
              className={rowIndex % 2 === 0 ? 'bg-[#162416]' : 'bg-[#101b10]'}
            >
              {columns.map((col) => (
                <td key={col} className="px-4 py-2 whitespace-nowrap">
                  {row[col] ?? '-'}
                </td>
              ))}
              {onAction && (
                <td className="px-4 py-2">
                  <button
                    onClick={() => onAction(row)}
                    className="bg-green-700 hover:bg-green-600 text-white px-3 py-1 rounded shadow"
                  >
                    {actionLabel || 'Ação'}
                  </button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

DataTable.propTypes = {
  data: PropTypes.array,
  columns: PropTypes.array,
  onAction: PropTypes.func,
  actionLabel: PropTypes.string,
};

export default DataTable;
