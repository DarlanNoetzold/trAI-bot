import React, { useEffect, useState } from 'react';
import { getAccountInfo, getAccountStatus } from '../../services/accountService';
import Loader from '../../components/common/Loader';

function AccountInfo() {
  const [info, setInfo] = useState(null);
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchAccount() {
      setLoading(true);
      try {
        const [infoRes, statusRes] = await Promise.all([
          getAccountInfo(),
          getAccountStatus()
        ]);
        setInfo(infoRes);
        setStatus(statusRes);
      } catch (error) {
        console.error('Erro ao buscar informações da conta:', error);
      } finally {
        setLoading(false);
      }
    }
    fetchAccount();
  }, []);

  if (loading) return <Loader />;

  return (
    <div>
      <h2 className="text-xl font-semibold mb-4">Informações da Conta</h2>

      {status && (
        <div className="mb-4">
          <p><strong>Status da Conta:</strong> {status.data ? 'Ativa' : 'Inativa'}</p>
        </div>
      )}

      {info && (
        <div>
          <h3 className="text-lg font-medium mb-2">Saldos:</h3>
          <table className="w-full text-left border">
            <thead>
              <tr>
                <th className="border p-2">Moeda</th>
                <th className="border p-2">Disponível</th>
                <th className="border p-2">Em Ordem</th>
              </tr>
            </thead>
            <tbody>
              {info.balances.filter(b => parseFloat(b.free) > 0 || parseFloat(b.locked) > 0).map((balance) => (
                <tr key={balance.asset}>
                  <td className="border p-2">{balance.asset}</td>
                  <td className="border p-2">{balance.free}</td>
                  <td className="border p-2">{balance.locked}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default AccountInfo;